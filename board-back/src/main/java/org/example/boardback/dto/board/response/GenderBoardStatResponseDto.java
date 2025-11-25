package org.example.boardback.dto.board.response;

import java.util.Map;

public record GenderBoardStatResponseDto(
        Map<String, Long> genderToCount
) { }
