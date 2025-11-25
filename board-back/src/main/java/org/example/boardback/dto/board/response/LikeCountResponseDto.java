package org.example.boardback.dto.board.response;

/**
 * 좋아요 개수 응답
 */
public record LikeCountResponseDto(
        Long boardId,
        int count,
        boolean likedByMe
) { }
