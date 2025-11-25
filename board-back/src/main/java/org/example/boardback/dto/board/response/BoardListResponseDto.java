package org.example.boardback.dto.board.response;

import java.util.List;

/**
 * 카테고리 / 단순 리스트 응답
 */
public record BoardListResponseDto(
        List<BoardSummaryDto> items,
        int total
) { }
