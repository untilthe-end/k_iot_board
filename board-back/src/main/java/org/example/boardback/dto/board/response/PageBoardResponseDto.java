package org.example.boardback.dto.board.response;

import java.util.List;

/**
 * 페이징 응답
 */
public record PageBoardResponseDto(
        List<BoardSummaryDto> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) { }
