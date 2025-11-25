package org.example.boardback.common.apis.board;

import org.example.boardback.common.apis.ApiBase;

public class BoardApi {
    private BoardApi() {}

    // ==================================================
    // Boards
    // ==================================================

    public static final String ROOT = ApiBase.BASE + "/boards";

    // 기본 CRUD
    public static final String ID_ONLY = "/{boardId}";                              // GET, PUT, DELETE
    public static final String BY_ID = ID_ONLY;                                     // 단일 게시글

    // 카테고리
    public static final String CATEGORY = "/category/{categoryId}";
    public static final String COUNT_BY_CATEGORY = "/category-count";

    // 검색
    public static final String SEARCH = "/search";

    // + 페이징 조회
    //   : GET /boards/page?page=0&size=10&sort=createdAt,desc
    //   > 첫 페이지에 10개의 게시물을 최신순(작성순+내림차순)으로 조회
    public static final String PAGE = "/page";

    // + 내가 쓴 글
    public static final String MY_BOARDS = "/me";

    // + 조회수 증가
    public static final String VIEW = ID_ONLY + "/view";

    // + 좋아요(LIKE) 기능
    public static final String LIKE = ID_ONLY + "/like";
    public static final String LIKE_CANCEL = ID_ONLY + "/like/cancel";
    public static final String LIKE_COUNT = ID_ONLY + "/like/count";

    // + 게시글 고정 (Pin / Notice 기능)
    public static final String PIN = ID_ONLY + "/pin";
    public static final String UNPIN = ID_ONLY + "/unpin";
    public static final String PINNED_LIST = ID_ONLY + "/pinned";

    // + 게시글 신고 (Report / Abuse)
    public static final String REPORT = ID_ONLY + "/report";

    // + 임시 저장
    public static final String DRAFT = "/draft";
    public static final String DRAFT_BY_ID = DRAFT + "/{draftId}";

    // + 통계
    public static final String STATS = "/stats";
    public static final String STATS_DAILY = STATS + "/daily";
    public static final String STATS_MONTHLY = STATS + "/monthly";
    public static final String STATS_GENDER = STATS + "/gender";
}
