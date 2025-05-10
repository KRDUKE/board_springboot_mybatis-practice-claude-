package com.dukefirstboard.board.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data   // Lombok 어노테이션: getter, setter, toString 등 자동 생성
public class CategoryDTO {
    private Long categoryId;           // 카테고리 ID (기본키)
    private String categoryName;       // 카테고리 이름
    private int categoryOrder;         // 카테고리 표시 순서
    private LocalDateTime createdAt;   // 생성 시간
    private LocalDateTime updatedAt;   // 수정 시간
}