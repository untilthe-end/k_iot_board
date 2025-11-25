package org.example.boardback.common.apis.board;

import org.example.boardback.common.apis.ApiBase;

public class BoardFileApi {
    private BoardFileApi() {}

    // 파일은 게시글(board)에 종속
    public static final String ROOT = ApiBase.BASE + "/board-files";

    // == boardId 기준 파일 기능 ==
    public static final String FILES_BY_BOARD = "/{boardId}/files";

    // 업로드
    public static final String UPLOAD = FILES_BY_BOARD;

    // 조회
    public static final String LIST = FILES_BY_BOARD;

    // 수정
    public static final String UPDATE = FILES_BY_BOARD;

    // == 파일 단일 접근: boardId 필요 X == //
    public static final String FILE_BY_ID = "/files/{fileId}";

    // 삭제
    public static final String DELETE = FILE_BY_ID;

    // 다운로드
    public static final String DOWNLOAD = FILE_BY_ID + "/download";
}
