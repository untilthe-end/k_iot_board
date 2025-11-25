package org.example.boardback.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.boardback.common.apis.board.BoardApi;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.board.request.*;
import org.example.boardback.dto.board.response.*;
import org.example.boardback.service.board.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*
  게시글 정보 CRUD
  : 생성 + 조회 + 수정 시 게시글 이미지 동시에 처리
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardLikeService boardLikeService;
    private final BoardDraftService boardDraftService;
    private final BoardPinService boardPinService;
    private final BoardReportService boardReportService;
    private final BoardStatService boardStatService;
// ============================================================
    // 1. 기본 CRUD
    // ============================================================

    // 단일 조회
    @GetMapping(BoardApi.BY_ID)
    public ResponseEntity<ResponseDto<BoardResponseDto>> getBoard(
            @PathVariable String boardId
    ) {
        ResponseDto<BoardResponseDto> data = boardService.getBoard(boardId);
        return ResponseEntity.ok(data);
    }

    // 생성
    @PostMapping
    public ResponseEntity<ResponseDto<BoardResponseDto>> createBoard(
            @Valid @RequestBody BoardCreateRequestDto request
    ) {
        ResponseDto<BoardResponseDto> data = boardService.createBoard(request);
        return ResponseEntity.ok(data);
    }

    // 수정
    @PutMapping(BoardApi.ID_ONLY)
    public ResponseEntity<ResponseDto<BoardResponseDto>> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequestDto request
    ) {
        ResponseDto<BoardResponseDto> data = boardService.updateBoard(boardId, request);
        return ResponseEntity.ok(data);
    }

    // 삭제
    @DeleteMapping(BoardApi.ID_ONLY)
    public ResponseEntity<ResponseDto<?>> deleteBoard(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardService.deleteBoard(boardId);
        return ResponseEntity.ok(result);
    }


    // ============================================================
    // 2. 검색 / 카테고리 / 페이징 / 내가 쓴 글
    // ============================================================

    @GetMapping(BoardApi.SEARCH)
    public ResponseEntity<ResponseDto<SearchBoardListResponseDto>> searchBoards(
            @Valid SearchBoardRequestDto request
    ) {
        ResponseDto<SearchBoardListResponseDto> data = boardService.searchBoards(request);
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.CATEGORY)
    public ResponseEntity<ResponseDto<BoardListResponseDto>> getBoardsByCategory(
            @PathVariable Long categoryId
    ) {
        ResponseDto<BoardListResponseDto> data = boardService.getBoardsByCategory(categoryId);
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.PAGE)
    public ResponseEntity<ResponseDto<PageBoardResponseDto>> getBoardsByPage(
            @Valid PageBoardRequestDto request
    ) {
        ResponseDto<PageBoardResponseDto> data = boardService.getBoardsByPage(request);
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.MY_BOARDS)
    public ResponseEntity<ResponseDto<BoardListResponseDto>> getMyBoards() {
        ResponseDto<BoardListResponseDto> data = boardService.getMyBoards();
        return ResponseEntity.ok(data);
    }


    // ============================================================
    // 3. 조회수 증가
    // ============================================================

    @PostMapping(BoardApi.VIEW)
    public ResponseEntity<ResponseDto<?>> increaseViewCount(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardService.increaseViewCount(boardId);
        return ResponseEntity.ok(result);
    }


    // ============================================================
    // 4. 좋아요 기능
    // ============================================================

    @PostMapping(BoardApi.LIKE)
    public ResponseEntity<ResponseDto<?>> likeBoard(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardLikeService.likeBoard(boardId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(BoardApi.LIKE_CANCEL)
    public ResponseEntity<ResponseDto<?>> cancelLike(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardLikeService.cancelLike(boardId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(BoardApi.LIKE_COUNT)
    public ResponseEntity<ResponseDto<LikeCountResponseDto>> likeCount(
            @PathVariable Long boardId
    ) {
        ResponseDto<LikeCountResponseDto> data = boardLikeService.likeCount(boardId);
        return ResponseEntity.ok(data);
    }


    // ============================================================
    // 5. 게시글 신고
    // ============================================================

    @PostMapping(BoardApi.REPORT)
    public ResponseEntity<ResponseDto<?>> reportBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody ReportBoardRequestDto request
    ) {
        ResponseDto<Void> result = boardReportService.reportBoard(boardId, request);
        return ResponseEntity.ok(result);
    }


    // ============================================================
    // 6. 게시글 고정(Pin)
    // ============================================================

    @PostMapping(BoardApi.PIN)
    public ResponseEntity<ResponseDto<?>> pinBoard(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardPinService.pin(boardId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(BoardApi.UNPIN)
    public ResponseEntity<ResponseDto<?>> unpinBoard(
            @PathVariable Long boardId
    ) {
        ResponseDto<Void> result = boardPinService.unpin(boardId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(BoardApi.PINNED_LIST)
    public ResponseEntity<ResponseDto<PinnedBoardListResponseDto>> pinnedBoards() {
        ResponseDto<PinnedBoardListResponseDto> data = boardPinService.getPinnedBoards();
        return ResponseEntity.ok(data);
    }


    // ============================================================
    // 7. 임시 저장(Draft)
    // ============================================================

    @PostMapping(BoardApi.DRAFT)
    public ResponseEntity<ResponseDto<DraftResponseDto>> saveDraft(
            @Valid @RequestBody DraftCreateRequestDto request
    ) {
        ResponseDto<DraftResponseDto> data = boardDraftService.saveDraft(request);
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.DRAFT_BY_ID)
    public ResponseEntity<ResponseDto<DraftResponseDto>> getDraft(
            @PathVariable Long draftId
    ) {
        ResponseDto<DraftResponseDto> data = boardDraftService.getDraft(draftId);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping(BoardApi.DRAFT_BY_ID)
    public ResponseEntity<ResponseDto<?>> deleteDraft(
            @PathVariable Long draftId
    ) {
        ResponseDto<Void> result = boardDraftService.deleteDraft(draftId);
        return ResponseEntity.ok(result);
    }


    // ============================================================
    // 8. 통계 (Stats)
    // ============================================================

    @GetMapping(BoardApi.STATS_DAILY)
    public ResponseEntity<ResponseDto<DailyBoardStatResponseDto>> dailyStats() {
        ResponseDto<DailyBoardStatResponseDto> data = boardStatService.dailyStats();
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.STATS_MONTHLY)
    public ResponseEntity<ResponseDto<MonthlyBoardStatResponseDto>> monthlyStats() {
        ResponseDto<MonthlyBoardStatResponseDto> data = boardStatService.monthlyStats();
        return ResponseEntity.ok(data);
    }

    @GetMapping(BoardApi.STATS_GENDER)
    public ResponseEntity<ResponseDto<GenderBoardStatResponseDto>> genderStats() {
        ResponseDto<GenderBoardStatResponseDto> data = boardStatService.genderStats();
        return ResponseEntity.ok(data);
    }
}