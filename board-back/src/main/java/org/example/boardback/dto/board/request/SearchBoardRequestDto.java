package org.example.boardback.dto.board.request;

import jakarta.validation.constraints.*;

/**
 * 게시글 검색 요청 (간단 버전)
 */
public record SearchBoardRequestDto(
        @NotBlank(message = "검색어를 입력하세요.")
        String q,

        // 검색 타입: title/content/author/all
        @Pattern(regexp = "^(title|content|author|all)$", message = "searchType은 title|content|author|all 중 하나여야 합니다.")
        String searchType
) { }