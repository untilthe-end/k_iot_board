package org.example.boardback.service.board.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.boardback.common.enums.ErrorCode;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board_file.BoardFileListDto;
import org.example.boardback.dto.board_file.BoardFileUpdateRequestDto;
import org.example.boardback.entity.board.Board;
import org.example.boardback.entity.file.BoardFile;
import org.example.boardback.entity.file.FileInfo;
import org.example.boardback.exception.FileStorageException;
import org.example.boardback.repository.board.BoardRepository;
import org.example.boardback.repository.file.BoardFileRepository;
import org.example.boardback.repository.file.FileInfoRepository;
import org.example.boardback.service.board.BoardFileService;
import org.example.boardback.service.impl.FileServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardFileServiceImpl implements BoardFileService {

    private final FileServiceImpl fileService;
    private final BoardFileRepository boardFileRepository;
    private final FileInfoRepository fileInfoRepository;
    private final BoardRepository boardRepository;

    private final int MAX_ATTACH = 5;

    @Transactional
    @Override
    public ResponseDto<Void> uploadBoardFiles(Long boardId, List<MultipartFile> files) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

        if (files.size() > MAX_ATTACH) {
            throw new IllegalArgumentException("최대 " + MAX_ATTACH + "개까지 업로드 가능");
        }

        int order = 0;
        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) {
                continue;
            }
            FileInfo info = fileService.saveBoardFile(boardId, mf);
            if (info == null) continue;

            BoardFile boardFile = BoardFile.of(board, info, order++);
            boardFileRepository.save(boardFile);
        }

        return ResponseDto.success("success");
    }

    @Override
    public ResponseDto<List<BoardFileListDto>> getFilesByBoard(Long boardId) {
        final String baseURL = "/api/file/download/";
        List<BoardFile> boardFiles = boardFileRepository.findByBoardIdOrderByDisplayOrderAsc(boardId);

        List<BoardFileListDto> result = boardFiles.stream()
                .map(BoardFile::getFileInfo)
                .filter(Objects::nonNull)
                .map(fileInfo -> BoardFileListDto.fromEntity(fileInfo, baseURL))
                .toList();

        return ResponseDto.success(result);
    }

    @Override
    public FileInfo getFileInfo(Long fileId) {
        FileInfo info = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new FileStorageException(ErrorCode.INTERNAL_ERROR));

        return info;
    }

    @Override
    public Path loadFile(Long fileId) {
        FileInfo fileInfo = getFileInfo(fileId); // ✔ 수정됨
        Path path = Paths.get(fileInfo.getFilePath());

        if (!Files.exists(path) || !Files.isReadable(path)) {
            throw new FileStorageException(ErrorCode.INTERNAL_ERROR);
        }
        return path;
    }

    @Override
    public HttpHeaders createDownloadHeaders(FileInfo info, Path path) {
        HttpHeaders headers = new HttpHeaders();

        String contentType = info.getContentType();
        if (contentType == null) {
            try {
                contentType = Files.probeContentType(path);
            } catch (Exception ignored) {}
        }
        headers.setContentType(MediaType.parseMediaType(
                contentType != null ? contentType : "application/octet-stream"
        ));

        String encodedName = URLEncoder.encode(info.getOriginalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""
                        + info.getOriginalName().replace("\"", "")
                        + "\"; "
                        + "filename*=UTF-8''" + encodedName
        );

        headers.setContentLength(info.getFileSize());

        return headers;
    }

    @Transactional
    @Override
    public ResponseDto<Void> deleteBoardFile(Long fileId) {
        BoardFile boardFile = boardFileRepository.findByFileInfoId(fileId)
                .orElseThrow(() -> new FileStorageException(ErrorCode.INTERNAL_ERROR));

        FileInfo fileInfo = boardFile.getFileInfo();

        boardFileRepository.delete(boardFile);
        fileService.deleteFile(fileInfo);

        return ResponseDto.success("success");
    }

    @Transactional
    @Override
    public ResponseDto<Void> updateBoardFiles(Long boardId, BoardFileUpdateRequestDto dto) {
        List<Long> keepIds = dto.getKeepFileIds() == null ? List.of() : dto.getKeepFileIds();
        List<MultipartFile> newFiles = dto.getNewFiles();

        List<BoardFile> currentFiles = boardFileRepository.findByBoardIdOrderByDisplayOrderAsc(boardId);

        List<BoardFile> deleteTargets = currentFiles.stream()
                .filter(boardFile -> {
                    FileInfo info = boardFile.getFileInfo();
                    return info == null || !keepIds.contains(info.getId());
                })
                .toList();

        for (BoardFile bf : deleteTargets) {
            boardFileRepository.delete(bf);
            FileInfo info = bf.getFileInfo();
            if (info != null) {
                fileService.deleteFile(info);
            }
        }

        if (newFiles != null && !newFiles.isEmpty()) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글이 없습니다."));

            int maxOrder = boardFileRepository.findByBoardIdOrderByDisplayOrderAsc(boardId)
                    .stream()
                    .mapToInt(BoardFile::getDisplayOrder)
                    .max()
                    .orElse(-1);

            for (MultipartFile mf : newFiles) {
                FileInfo info = fileService.saveBoardFile(boardId, mf);
                BoardFile bf = BoardFile.of(board, info, ++maxOrder);
                boardFileRepository.save(bf);
            }
        }

        return ResponseDto.success("success");
    }
}