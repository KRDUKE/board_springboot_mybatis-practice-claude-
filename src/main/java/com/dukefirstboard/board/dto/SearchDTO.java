package com.dukefirstboard.board.dto;

import lombok.Data;

/**
 * 검색 조건을 담는 DTO 클래스
 */
@Data
public class SearchDTO {
    private String searchType;  // 검색 유형 (title, content, writer, titleAndContent)
    private String searchKeyword;  // 검색 키워드
    private Long categoryId;  // 카테고리 ID (선택적)
}