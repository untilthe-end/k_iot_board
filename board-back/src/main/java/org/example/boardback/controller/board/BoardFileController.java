package org.example.boardback.controller.board;

import lombok.RequiredArgsConstructor;
import org.example.boardback.common.apis.board.BoardFileApi;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board_file.BoardFileListDto;
import org.example.boardback.dto.board_file.BoardFileUpdateRequestDto;
import org.example.boardback.entity.file.FileInfo;
import org.example.boardback.service.board.impl.BoardFileServiceImpl;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(BoardFileApi.ROOT)
@RequiredArgsConstructor
/*
  게시글 파일 정보 CRUD
  : 게시글 이미지 업로드/수정/삭제/순서 변경 등
 */
public class BoardFileController {
    private final BoardFileServiceImpl boardFileService;

    @PostMapping(BoardFileApi.UPLOAD)
    public ResponseEntity<ResponseDto<Void>> uploadBoardFiles(
            @PathVariable Long boardId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        ResponseDto<Void> result = boardFileService.uploadBoardFiles(boardId, files);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping(BoardFileApi.LIST)
    public ResponseEntity<ResponseDto<List<BoardFileListDto>>> getFilesByBoard(
            @PathVariable Long boardId
    ) {
        ResponseDto<List<BoardFileListDto>> files = boardFileService.getFilesByBoard(boardId);

        return ResponseEntity.ok(files); // 200 OK
    }

    @GetMapping(BoardFileApi.DOWNLOAD)
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        // service 에서 파일 정보 조회
        FileInfo info = boardFileService.getFileInfo(fileId);

        // 파일 경로 획득 + 존재 여부 검증
        Path path = boardFileService.loadFile(fileId);

        // Resource 변환
        Resource resource = new PathResource(path);

        // 다운로드에 필요한 헤더 생성 (파일명 인코딩, MIME 타입 등)
        HttpHeaders headers = boardFileService.createDownloadHeaders(info, path);

        // 응답 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @DeleteMapping(BoardFileApi.DELETE)
    public ResponseEntity<ResponseDto<Void>> deleteBoardFile(@PathVariable Long fileId) {
        ResponseDto<Void> result = boardFileService.deleteBoardFile(fileId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(
            value = BoardFileApi.UPDATE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateBoardFiles(
            @PathVariable Long boardId,
            // ModelAttribute
            // : 쿼리 파라미터나 HTML 폼 데이터를 Java 객체로 바인딩할 때 사용
            // MultipartFile + List<Long> 조합 시 JSON + 파일 혼합이 불가능
            //      >> FormData 기반 요청에는 @RequestBody 사용 불가
            //      >> @ModelAttribute가 가장 안정적인 방식
            @ModelAttribute BoardFileUpdateRequestDto dto) {
        ResponseDto<Void> result = boardFileService.updateBoardFiles(boardId, dto);
        return ResponseEntity.ok(result);
    }
}
