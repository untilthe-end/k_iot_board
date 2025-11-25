package org.example.boardback.service.board.impl;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.LikeCountResponseDto;
import org.example.boardback.service.board.BoardLikeService;
import org.springframework.stereotype.Service;

@Service
public class BoardLikeServiceImpl implements BoardLikeService {
    @Override
    public ResponseDto<Void> likeBoard(Long boardId) {
        return null;
    }

    @Override
    public ResponseDto<Void> cancelLike(Long boardId) {
        return null;
    }

    @Override
    public ResponseDto<LikeCountResponseDto> likeCount(Long boardId) {
        return null;
    }
}
