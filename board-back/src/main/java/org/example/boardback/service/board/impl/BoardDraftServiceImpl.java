package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.DraftCreateRequestDto;
import org.example.boardback.dto.board.response.DraftResponseDto;
import org.example.boardback.service.board.BoardDraftService;
import org.springframework.stereotype.Service;

@Service
public class BoardDraftServiceImpl implements BoardDraftService {
    @Override
    public ResponseDto<DraftResponseDto> saveDraft(DraftCreateRequestDto request) {
        return null;
    }

    @Override
    public ResponseDto<DraftResponseDto> getDraft(Long draftId) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDraft(Long draftId) {
        return null;
    }
}
