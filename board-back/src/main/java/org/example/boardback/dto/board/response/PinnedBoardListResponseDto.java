package org.example.boardback.dto.board.response;

import java.util.List;

/**
 * 고정 게시글 목록 응답
 */
public record PinnedBoardListResponseDto(
        List<BoardSummaryDto> pinned
) { }