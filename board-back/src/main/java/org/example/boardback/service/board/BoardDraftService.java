package org.example.boardback.service.board;

import jakarta.validation.Valid;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.DraftCreateRequestDto;
import org.example.boardback.dto.board.response.DraftResponseDto;

public interface BoardDraftService {
    ResponseDto<DraftResponseDto> saveDraft(@Valid DraftCreateRequestDto request);
    ResponseDto<DraftResponseDto> getDraft(Long draftId);
    ResponseDto<Void> deleteDraft(Long draftId);
}
