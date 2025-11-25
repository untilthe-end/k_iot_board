package org.example.boardback.dto.board.response;

import java.util.Map;

public record MonthlyBoardStatResponseDto(
        Map<String, Long> monthToCount
) { }
