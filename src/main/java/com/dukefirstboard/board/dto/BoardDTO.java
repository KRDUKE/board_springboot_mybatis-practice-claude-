package com.dukefirstboard.board.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 정보를 담는 DTO 클래스
 * 데이터베이스의 board_table과 매핑됨
 */
@Data
public class BoardDTO {
    private Long id;                       // 게시글 고유 ID
    private String boardWriter;            // 작성자
    private String boardPass;              // 게시글 비밀번호
    private String boardTitle;             // 제목
    private String boardContents;          // 내용
    private int boardHits;                 // 조회수
    private LocalDateTime createdAt;       // 작성 시간
    private int fileAttached;              // 파일 첨부 여부(0: 없음, 1: 있음)
    private List<MultipartFile> boardFile; // 첨부 파일 (다중 파일 업로드 지원)

    // 추가된 필드: 카테고리 정보
    private Long categoryId;               // 카테고리 ID (외래 키)
    private String categoryName;           // 카테고리 이름 (조회 시 JOIN을 통해 가져옴)
}