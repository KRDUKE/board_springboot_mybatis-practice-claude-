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
## 2. 시작 페이지 출력과 DevTools 적용
문제: 시작 페이지 출력 안 됨
원인: JDK 미설치 Hannah: JDK가 설치되지 않아 발생.
해결: Oracle JDK 24 설치 후 JAVA_HOME 환경 변수 설정.
DevTools 적용
Spring Boot DevTools를 프로젝트에 추가.
개발 도구에 따라 Gradle 또는 Maven에서 의존성 추가 방식이 다름.
검색을 통해 IDE별 적용 방법 확인 권장.
### 3. Controller, Service, Repository 클래스 생성
패키지 구조
com.dukefirstboard.board 패키지 내에 하위 패키지 생성:
controller
service
dto
repository
클래스 정의
DTO 클래스: BoardDTO - 게시글 데이터 전송 객체.
Repository 클래스: BoardRepository - 데이터베이스 상호작용 담당.
Service 클래스: BoardService - 비즈니스 로직 처리.
Controller 클래스: BoardController - HTTP 요청 처리.
어노테이션 추가
BoardService: @Service 추가.
BoardRepository: @Repository 추가.
### 4. Lombok 설치와 의존성 추가
프로젝트에 Lombok 의존성 추가.
IDE에서 Lombok 플러그인 설치 및 설정 활성화.
### 5. DTO 클래스 정의
BoardDTO에 필드 정의:
글번호, 작성자, 비밀번호, 제목, 내용, 조회수, 작성 시간.
Lombok 어노테이션 추가:
@Getter, @Setter, @ToString 적용.
### 6. 의존 관계 주입
생성자 주입 방식으로 의존성 주입 설정.
BoardController는 BoardService 주입.
BoardService는 BoardRepository 주입.
Lombok의 @RequiredArgsConstructor 활용.
### 7. 컨트롤러 클래스 작성
BoardController 생성.
생성자 주입으로 BoardService 주입.
BoardService에도 @RequiredArgsConstructor 추가.

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
