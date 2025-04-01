package com.dukefirstboard.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// @Getter: Lombok 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성.
// 예: getId(), getBoardWriter() 등이 생성됨.
@Getter
// @Setter: Lombok 어노테이션으로, 모든 필드에 대한 setter 메서드를 자동 생성.
// 예: setId(), setBoardWriter() 등이 생성됨.
@Setter
// @ToString: Lombok 어노테이션으로, 객체를 문자열로 표현하는 toString() 메서드를 자동 생성.
// 출력 예: "BoardDTO(id=1, boardWriter=John, ...)"
@ToString
public class BoardDTO {
    // 게시글의 고유 번호 (Primary Key).
    // 데이터베이스에서 자동 생성되는 값으로, 게시글을 식별하는 데 사용.
    private Long id;

    // 게시글 작성자의 이름.
    // 사용자가 입력한 값을 저장하며, 작성자를 표시할 때 사용.
    private String boardWriter;

    // 게시글 비밀번호.
    // 게시글 수정/삭제 시 인증용으로 사용자가 입력한 값.
    private String boardPass;

    // 게시글 제목.
    // 사용자가 입력한 게시글의 주제를 나타냄.
    private String boardTitle;

    // 게시글 내용.
    // 사용자가 작성한 본문 텍스트를 저장.
    private String boardContents;

    // 게시글 조회수.
    // 상세 페이지 조회 시 증가하며, 게시글의 인기를 나타냄.
    private int boardHits;

    // 게시글 작성 시간.
    // 데이터베이스에서 생성된 날짜/시간을 문자열로 저장 (예: "2025-04-01").
    private String createdAt;

    // 파일 첨부 여부를 나타내는 플래그.
    // 0: 파일 없음, 1: 파일 있음. 파일 첨부 상태를 확인하는 데 사용.
    private int fileAttached;

    // 첨부 파일 리스트.
    // MultipartFile 타입으로, 사용자가 업로드한 파일 데이터를 저장.
    // List로 여러 파일을 지원하며, 컨트롤러에서 받아 서비스로 전달.
    private List<MultipartFile> boardFile;
}