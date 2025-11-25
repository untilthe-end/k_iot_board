package org.example.boardback.common.apis.board;

import org.example.boardback.common.apis.ApiBase;

public class BoardCommentApi {
    private BoardCommentApi() {}

    // ==================================================
    // Comment
    // ==================================================
    public static final String ROOT = ApiBase.BASE + "/boards/{boardId}/comments";
    public static final String COMMENTS_BY_ID = "/{commentId}";
}
