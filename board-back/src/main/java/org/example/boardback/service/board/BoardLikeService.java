package org.example.boardback.service.board;

import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.response.LikeCountResponseDto;

public interface BoardLikeService {
    ResponseDto<Void> likeBoard(Long boardId);
    ResponseDto<Void> cancelLike(Long boardId);
    ResponseDto<LikeCountResponseDto> likeCount(Long boardId);
}
