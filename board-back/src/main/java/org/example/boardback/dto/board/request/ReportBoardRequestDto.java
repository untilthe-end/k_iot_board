package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;

/**
 * 신고 요청 DTO
 */
public record ReportBoardRequestDto(
        @NotBlank(message = "신고 사유를 입력하세요.")
        @Size(max = 500, message = "신고 사유는 최대 500자입니다.")
        String reason
) { }