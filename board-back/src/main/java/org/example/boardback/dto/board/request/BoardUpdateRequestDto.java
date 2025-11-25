package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * 게시글 수정 요청
 */
public record BoardUpdateRequestDto(
        @NotBlank(message = "제목을 입력하세요.")
        @Size(max = 150, message = "제목은 최대 150자입니다.")
        String title,

        @NotBlank(message = "내용을 입력하세요.")
        String content,

        @NotNull(message = "카테고리 아이디가 필요합니다.")
        Long categoryId,

        // 파일 관련 변경(프론트: 새로 업로드된 fileIds + 삭제할 fileIds 등)
        List<Long> keepFileIds,
        List<Long> deleteFileIds,
        List<Long> addFileIds
) { }