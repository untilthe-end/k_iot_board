package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.DailyBoardStatResponseDto;
import org.example.boardback.dto.board.response.GenderBoardStatResponseDto;
import org.example.boardback.dto.board.response.MonthlyBoardStatResponseDto;
import org.example.boardback.service.board.BoardStatService;
import org.springframework.stereotype.Service;

@Service
public class BoardStatServiceImpl implements BoardStatService {
    @Override
    public ResponseDto<DailyBoardStatResponseDto> dailyStats() {
        return null;
    }

    @Override
    public ResponseDto<MonthlyBoardStatResponseDto> monthlyStats() {
        return null;
    }

    @Override
    public ResponseDto<GenderBoardStatResponseDto> genderStats() {
        return null;
    }
}
