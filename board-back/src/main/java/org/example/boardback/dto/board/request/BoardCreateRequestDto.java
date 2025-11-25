package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;
import java.util.List;
/**
 * 게시글 생성 요청
 */
public record BoardCreateRequestDto(
        @NotBlank(message = "제목을 입력하세요.")
        @Size(max = 150, message = "제목은 최대 150자입니다.")
        String title,

        @NotBlank(message = "내용을 입력하세요.")
        String content,

        @NotNull(message = "카테고리 아이디가 필요합니다.")
        Long categoryId,

        // optional: 파일 id 리스트(이미 업로드되어 저장된 fileInfo id 들)
        List<Long> fileIds
) { }