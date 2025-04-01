package com.dukefirstboard.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// @Getter: Lombok 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성.
// 예: getId(), getBoardId() 등이 생성됨.
@Getter
// @Setter: Lombok 어노테이션으로, 모든 필드에 대한 setter 메서드를 자동 생성.
// 예: setId(), setBoardId() 등이 생성됨.
@Setter
// @ToString: Lombok 어노테이션으로, 객체를 문자열로 표현하는 toString() 메서드를 자동 생성.
// 출력 예: "BoardFileDTO(id=1, boardId=10, originalFileName=image.jpg, ...)"
@ToString
public class BoardFileDTO {
    // 파일 정보의 고유 번호 (Primary Key).
    // 데이터베이스에서 자동 생성되며, 각 파일 레코드를 식별하는 데 사용.
    private Long id;

    // 이 파일이 첨부된 게시글의 ID (Foreign Key).
    // BoardDTO의 id와 연결되어, 어떤 게시글에 속한 파일인지 나타냄.
    private Long boardId;

    // 사용자가 업로드한 파일의 원래 이름.
    // 예: "KakaoTalk_20250324_222702116.jpg"처럼 사용자가 올린 파일명을 저장.
    private String originalFileName;

    // 서버에 저장된 파일의 이름.
    // 중복 방지와 관리를 위해 생성된 고유 이름 (예: "1743514819629-KakaoTalk_20250324_222702116.jpg").
    private String storedFileName;
}