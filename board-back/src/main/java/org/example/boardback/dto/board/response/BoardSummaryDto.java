package org.example.boardback.dto.board.response;

import java.time.Instant;

/**
 * 게시글 리스트 아이템(간단 요약)
 */
public record BoardSummaryDto(
        Long id,
        String title,
        String excerpt,
        Long writerId,
        String writerNickname,
        Long categoryId,
        String categoryName,
        long viewCount,
        int likeCount,
        int commentCount,
        boolean pinned,
        Instant createdAt
) { }
