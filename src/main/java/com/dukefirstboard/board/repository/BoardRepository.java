package com.dukefirstboard.board.repository;

import com.dukefirstboard.board.dto.BoardDTO;
import com.dukefirstboard.board.dto.BoardFileDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository: 이 클래스가 Spring의 데이터 접근 계층(Repository)임을 나타냄.
// Spring이 이 클래스를 빈으로 관리하며, 데이터베이스 작업을 처리.
@Repository
// @RequiredArgsConstructor: Lombok 어노테이션으로, final 필드(sql)에 대한 생성자를 자동 생성.
// 의존성 주입을 통해 SqlSessionTemplate을 받아옴.
@RequiredArgsConstructor
public class BoardRepository {
    // MyBatis의 SqlSessionTemplate 객체. SQL 쿼리를 실행하는 데 사용.
    // application.yml과 mybatis-config.xml 설정을 통해 데이터베이스와 연결됨.
    private final SqlSessionTemplate sql;

    // 게시글을 데이터베이스에 저장하는 메서드.
    // 매개변수: BoardDTO (저장할 게시글 데이터).
    // 반환: 저장된 BoardDTO (id가 DB에서 생성된 값으로 업데이트됨).
    public BoardDTO save(BoardDTO boardDTO) {
        // "Board.save"라는 MyBatis 매퍼 쿼리를 실행해 게시글을 삽입.
        // BoardMapper.xml의 <insert id="save"> 쿼리와 연결됨.
        sql.insert("Board.save", boardDTO);
        return boardDTO;
    }

    // 모든 게시글 목록을 조회하는 메서드.
    // 반환: List<BoardDTO> (데이터베이스의 모든 게시글 데이터).
    public List<BoardDTO> findAll() {
        // "Board.findAll" 쿼리를 실행해 게시글 목록을 가져옴.
        // BoardMapper.xml의 <select id="findAll"> 쿼리와 연결됨.
        return sql.selectList("Board.findAll");
    }

    // 게시글의 조회수를 증가시키는 메서드.
    // 매개변수: id (조회수를 증가시킬 게시글의 ID).
    public void updateHits(Long id) {
        // "Board.updateHits" 쿼리를 실행해 조회수를 1 증가.
        // BoardMapper.xml의 <update id="updateHits"> 쿼리와 연결됨.
        sql.update("Board.updateHits", id);
    }

    // 특정 게시글의 상세 정보를 조회하는 메서드.
    // 매개변수: id (조회할 게시글의 ID).
    // 반환: BoardDTO (해당 게시글의 데이터).
    public BoardDTO findById(Long id) {
        // "Board.findById" 쿼리를 실행해 단일 게시글을 가져옴.
        // BoardMapper.xml의 <select id="findById"> 쿼리와 연결됨.
        return sql.selectOne("Board.findById", id);
    }

    // 게시글을 수정하는 메서드.
    // 매개변수: BoardDTO (수정할 게시글 데이터).
    public void update(BoardDTO boardDTO) {
        // "Board.update" 쿼리를 실행해 게시글을 업데이트.
        // BoardMapper.xml의 <update id="update"> 쿼리와 연결됨.
        sql.update("Board.update", boardDTO);
    }

    // 게시글을 삭제하는 메서드.
    // 매개변수: id (삭제할 게시글의 ID).
    public void delete(Long id) {
        // "Board.delete" 쿼리를 실행해 게시글을 삭제.
        // BoardMapper.xml의 <delete id="delete"> 쿼리와 연결됨.
        sql.delete("Board.delete", id);
    }

    // 첨부 파일 정보를 데이터베이스에 저장하는 메서드.
    // 매개변수: BoardFileDTO (저장할 파일 데이터).
    public void saveFile(BoardFileDTO boardFileDTO) {
        // "Board.saveFile" 쿼리를 실행해 파일 정보를 삽입.
        // BoardMapper.xml의 <insert id="saveFile"> 쿼리와 연결됨.
        sql.insert("Board.saveFile", boardFileDTO);
    }

    // 특정 게시글에 첨부된 파일 목록을 조회하는 메서드.
    // 매개변수: id (게시글 ID).
    // 반환: List<BoardFileDTO> (해당 게시글에 연결된 파일 데이터 목록).
    public List<BoardFileDTO> findFile(Long id) {
        // "Board.findFile" 쿼리를 실행해 파일 목록을 가져옴.
        // BoardMapper.xml의 <select id="findFile"> 쿼리와 연결됨.
        return sql.selectList("Board.findFile", id);
    }
    /**
     * 특정 카테고리의 게시글 목록을 조회하는 메서드
     * @param categoryId 조회할 카테고리 ID
     * @return 해당 카테고리의 게시글 목록
     */
    public List<BoardDTO> findByCategory(Long categoryId) {
        return sql.selectList("Board.findByCategory", categoryId);
    }
}