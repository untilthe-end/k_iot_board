package org.example.boardback.dto.board.response;

import java.time.Instant;

/**
 * 게시글 상세 응답
 */
public record BoardResponseDto(
        Long id,
        String title,
        String content,
        long viewCount,
        boolean pinned,
        Long writerId,
        String writerNickname,
        Long categoryId,
        String categoryName,
        int likeCount,
        int commentCount,
        Instant createdAt,
        Instant updatedAt
) {
    public static BoardResponseDto of(
            Long id,
            String title,
            String content,
            long viewCount,
            boolean pinned,
            Long writerId,
            String writerNickname,
            Long categoryId,
            String categoryName,
            int likeCount,
            int commentCount,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new BoardResponseDto(id, title, content, viewCount, pinned,
                writerId, writerNickname, categoryId, categoryName,
                likeCount, commentCount, createdAt, updatedAt);
    }
}