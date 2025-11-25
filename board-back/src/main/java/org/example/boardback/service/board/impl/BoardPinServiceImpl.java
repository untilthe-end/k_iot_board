package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.PinnedBoardListResponseDto;
import org.example.boardback.service.board.BoardPinService;
import org.springframework.stereotype.Service;

@Service
public class BoardPinServiceImpl implements BoardPinService {
    @Override
    public ResponseDto<Void> pin(Long boardId) {
        return null;
    }

    @Override
    public ResponseDto<Void> unpin(Long boardId) {
        return null;
    }

    @Override
    public ResponseDto<PinnedBoardListResponseDto> getPinnedBoards() {
        return null;
    }
}
