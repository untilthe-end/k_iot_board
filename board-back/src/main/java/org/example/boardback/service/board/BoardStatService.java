package org.example.boardback.service.board;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.DailyBoardStatResponseDto;
import org.example.boardback.dto.board.response.GenderBoardStatResponseDto;
import org.example.boardback.dto.board.response.MonthlyBoardStatResponseDto;

public interface BoardStatService {
    ResponseDto<DailyBoardStatResponseDto> dailyStats();
    ResponseDto<MonthlyBoardStatResponseDto> monthlyStats();
    ResponseDto<GenderBoardStatResponseDto> genderStats();
}
