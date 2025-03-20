## 내용
스프링부트로 게시판 만들어보기

## 개발환경
- IntelliJ IDEA Community Edition 2023.3.3
- Amazon Corretto 21
- mysql community server 8.0
- spring boot 3.1.8
- mybatis framework
- thymeleaf

## dependencies
- Spring Web
    - MVC 패턴 구현을 위한
- Lombok
- Spring Boot DevTools
    - 코드 수정시 자동으로 서버 재시작

DB 연동시(뒷부분에서 추가 예정)
- MyBatis Framework
- MySQL Driver

## 개발내용 적기
1일차 프로젝트 세팅

![image](https://github.com/user-attachments/assets/e6469aef-3fdd-4ab8-82d5-da4b850b693b)

2일차 
2.시작 페이지출력과 devtools 적용
 시작 페이지가 출력이 안되는 문제는 jdk 미설치 때문이었음
 devtools적용은 어떤 툴이냐에 따라 gradle에서 적용이 달라지므로 검색을 활용할것
3.Controller, Service 클래스 만들어서 기본적인 기능 선언을 함
 보드 패키지 내부에서 Service, DTO, repository 패키지를 만들고 각 클래스를 정의함
 DTO클래스: 보드 DTO
 레퍼지토리 클래스: 보드 레퍼지토리
 서비스 클래스: 보드 서비스
 각 클래스에 어노테이션 추가
 서비스 클래스에는 `@Service`
 레퍼지토리 클래스에는 `@Repository`
이후 롬복 설치와 의존성 추가
 DTO 클래스에 다음 필드 정의:
 글번호
 작성자
 비밀번호
 제목
 내용
 조회수
 작성 시간
 각 필드에 대해 롬복의 어노테이션 추가:
 게터, 세터, 투 스트링
6. 의존 관계 주입
 컨트롤러 클래스는 서비스 클래스를, 서비스 클래스는 레퍼지토리 클래스를 활용해야 함.
 의존 관계 주입 방식:
 생성자 주입 사용
 롬복의 `@RequiredArgsConstructor` 어노테이션 활용 [18]
7. 컨트롤러 클래스 작성
 보드 컨트롤러 클래스 생성.
 생성자 주입 방식으로 서비스 클래스 주입.
 서비스 클래스에도 `@RequiredArgsConstructor` 추가.

## table 정의
```sql
-- board_table
 drop table if exists board_table;
 create table board_table
 (
	id bigint primary key auto_increment,
    boardTitle varchar(50),
    boardWriter varchar(20),
    boardPass varchar(20),
    boardContents varchar(500),
    boardHits int default 0,
    createdAt datetime default now(), 
    fileAttached int default 0
);
-- board_file_table
drop table if exists board_file_table;
create table board_file_table
(
    id	bigint auto_increment primary key,
    originalFileName varchar(100),
    storedFileName varchar(100),
    boardId bigint,
    constraint fk_board_file foreign key(boardId) references board_table(id) on delete cascade
);
```
