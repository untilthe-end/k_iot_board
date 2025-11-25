package org.example.boardback.service.board;

import jakarta.validation.Valid;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.ReportBoardRequestDto;

public interface BoardReportService {
    ResponseDto<Void> reportBoard(Long boardId, @Valid ReportBoardRequestDto request);
}
