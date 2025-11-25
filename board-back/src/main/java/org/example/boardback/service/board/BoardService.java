package org.example.boardback.service.board;

import jakarta.validation.Valid;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.BoardCreateRequestDto;
import org.example.boardback.dto.board.request.BoardUpdateRequestDto;
import org.example.boardback.dto.board.request.PageBoardRequestDto;
import org.example.boardback.dto.board.request.SearchBoardRequestDto;
import org.example.boardback.dto.board.response.BoardListResponseDto;
import org.example.boardback.dto.board.response.BoardResponseDto;
import org.example.boardback.dto.board.response.PageBoardResponseDto;
import org.example.boardback.dto.board.response.SearchBoardListResponseDto;

public interface BoardService {
    ResponseDto<BoardResponseDto> getBoard(Long boardId);
    ResponseDto<BoardResponseDto> createBoard(@Valid BoardCreateRequestDto request);
    ResponseDto<BoardResponseDto> updateBoard(Long boardId, @Valid BoardUpdateRequestDto request);
    ResponseDto<Void> deleteBoard(Long boardId);

    ResponseDto<SearchBoardListResponseDto> searchBoards(@Valid SearchBoardRequestDto request);
    ResponseDto<BoardListResponseDto> getBoardsByCategory(Long categoryId);
    ResponseDto<PageBoardResponseDto> getBoardsByPage(@Valid PageBoardRequestDto request);
    ResponseDto<BoardListResponseDto> getMyBoards();

    ResponseDto<Void> increaseViewCount(Long boardId);
}
