package com.dukefirstboard.board.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int page;           // 현재 페이지 번호
    private int size;           // 페이지당 게시글 수
    private int totalPages;     // 전체 페이지 수
    private long totalElements; // 전체 게시글 수
    private int startPage;      // 시작 페이지 (페이지 그룹)
    private int endPage;        // 끝 페이지 (페이지 그룹)
    private boolean prev;       // 이전 페이지 그룹 존재 여부
    private boolean next;       // 다음 페이지 그룹 존재 여부

    // 기본 생성자: 기본값 설정
    public PageDTO() {
        this.page = 1;
        this.size = 10;
    }

    // 페이지 계산을 위한 메서드
    public void calculatePageInfo() {
        // 전체 페이지 수 계산
        this.totalPages = (int) Math.ceil((double) totalElements / size);

        // 페이지 그룹 설정 (한 화면에 10개의 페이지 번호 표시)
        int pageGroup = 10;

        // 현재 페이지가 속한 페이지 그룹의 시작과 끝
        this.startPage = ((page - 1) / pageGroup) * pageGroup + 1;
        this.endPage = Math.min(startPage + pageGroup - 1, totalPages);

        // 이전/다음 페이지 그룹 버튼 표시 여부
        this.prev = startPage > 1;
        this.next = endPage < totalPages;
    }

    // 페이지에 적용할 LIMIT 절의 OFFSET 계산
    public int getOffset() {
        return (page - 1) * size;
    }
}