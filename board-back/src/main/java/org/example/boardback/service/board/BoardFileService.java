package org.example.boardback.service.board;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board_file.BoardFileListDto;
import org.example.boardback.dto.board_file.BoardFileUpdateRequestDto;
import org.example.boardback.entity.file.FileInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface BoardFileService {
    ResponseDto<Void> uploadBoardFiles(Long boardId, List<MultipartFile> files);
    ResponseDto<List<BoardFileListDto>> getFilesByBoard(Long boardId);
    FileInfo getFileInfo(Long fileId);
    Path loadFile(Long fileId);
    HttpHeaders createDownloadHeaders(FileInfo info, Path path);
    ResponseDto<Void> deleteBoardFile(Long fileId);
    ResponseDto<Void> updateBoardFiles(Long boardId, BoardFileUpdateRequestDto dto);
}
