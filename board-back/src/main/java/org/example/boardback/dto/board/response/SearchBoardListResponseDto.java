package org.example.boardback.dto.board.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchBoardListResponseDto(

        @NotNull(message = "게시글 목록은 null일 수 없습니다.")
        List<BoardSummaryDto> boards,

        @Min(value = 0, message = "전체 개수는 0 이상이어야 합니다.")
        long totalCount,

        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        int page,

        @Min(value = 1, message = "페이지 크기는 최소 1 이상이어야 합니다.")
        int size,

        @Min(value = 1, message = "전체 페이지 수는 최소 1 이상이어야 합니다.")
        int totalPages,

        @NotEmpty(message = "검색 키워드는 반드시 입력되어야 합니다.")
        String keyword,

        Long categoryId

) {}
