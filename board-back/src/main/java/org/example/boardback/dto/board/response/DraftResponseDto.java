package org.example.boardback.dto.board.response;

import java.time.Instant;

/**
 * 임시저장 응답
 */
public record DraftResponseDto(
        Long id,
        String title,
        String content,
        Long writerId,
        String writerNickname,
        Instant createdAt,
        Instant updatedAt
) {
    public static DraftResponseDto from(Long id, String title, String content,
                                        Long writerId, String writerNickname,
                                        Instant createdAt, Instant updatedAt) {
        return new DraftResponseDto(id, title, content, writerId, writerNickname, createdAt, updatedAt);
    }
}
