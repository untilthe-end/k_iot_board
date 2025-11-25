package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.BoardCreateRequestDto;
import org.example.boardback.dto.board.request.BoardUpdateRequestDto;
import org.example.boardback.dto.board.request.PageBoardRequestDto;
import org.example.boardback.dto.board.request.SearchBoardRequestDto;
import org.example.boardback.dto.board.response.BoardListResponseDto;
import org.example.boardback.dto.board.response.BoardResponseDto;
import org.example.boardback.dto.board.response.PageBoardResponseDto;
import org.example.boardback.dto.board.response.SearchBoardListResponseDto;
import org.example.boardback.service.board.BoardService;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {
    @Override
    public ResponseDto<BoardResponseDto> getBoard(String boardId) {
        return null;
    }

    @Override
    public ResponseDto<BoardResponseDto> createBoard(BoardCreateRequestDto request) {
        return null;
    }

    @Override
    public ResponseDto<BoardResponseDto> updateBoard(Long boardId, BoardUpdateRequestDto request) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteBoard(Long boardId) {
        return null;
    }

    @Override
    public ResponseDto<SearchBoardListResponseDto> searchBoards(SearchBoardRequestDto request) {
        return null;
    }

    @Override
    public ResponseDto<BoardListResponseDto> getBoardsByCategory(Long categoryId) {
        return null;
    }

    @Override
    public ResponseDto<PageBoardResponseDto> getBoardsByPage(PageBoardRequestDto request) {
        return null;
    }

    @Override
    public ResponseDto<BoardListResponseDto> getMyBoards() {
        return null;
    }

    @Override
    public ResponseDto<Void> increaseViewCount(Long boardId) {
        return null;
    }
}