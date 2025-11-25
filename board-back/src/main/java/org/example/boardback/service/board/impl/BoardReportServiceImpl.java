package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.ReportBoardRequestDto;
import org.example.boardback.service.board.BoardReportService;
import org.springframework.stereotype.Service;

@Service
public class BoardReportServiceImpl implements BoardReportService {
    @Override
    public ResponseDto<Void> reportBoard(Long boardId, ReportBoardRequestDto request) {
        return null;
    }
}
