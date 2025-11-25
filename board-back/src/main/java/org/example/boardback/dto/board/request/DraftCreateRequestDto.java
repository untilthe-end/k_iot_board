package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;

/**
 * Draft 생성 요청
 */
public record DraftCreateRequestDto(
        @Size(max = 150, message = "제목은 최대 150자입니다.")
        String title,

        String content
) { }