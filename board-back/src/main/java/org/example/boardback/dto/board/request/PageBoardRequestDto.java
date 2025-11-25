package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;

/**
 * 페이징 조회 요청
 */
public record PageBoardRequestDto(
        @Min(value = 0, message = "page는 0 이상이어야 합니다.")
        int page,
        @Min(value = 1, message = "size는 1 이상이어야 합니다.")
        @Max(value = 200, message = "size는 최대 200입니다.")
        int size,
        // sort 예: createdAt,desc
        @NotBlank(message = "정렬 기준을 지정하세요.")
        String sort
) { }
