package org.example.boardback.service.board;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.PinnedBoardListResponseDto;

public interface BoardPinService {
    ResponseDto<Void> pin(Long boardId);
    ResponseDto<Void> unpin(Long boardId);
    ResponseDto<PinnedBoardListResponseDto> getPinnedBoards();
}
