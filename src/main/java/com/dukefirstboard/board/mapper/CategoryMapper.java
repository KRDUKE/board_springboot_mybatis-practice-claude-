package com.dukefirstboard.board.mapper;

import com.dukefirstboard.board.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 카테고리 관련 데이터베이스 연산을 처리하는 매퍼 인터페이스
 */
@Mapper
public interface CategoryMapper {
    /**
     * 모든 카테고리 목록을 조회
     * @return 카테고리 목록
     */
    List<CategoryDTO> findAll();

    /**
     * 특정 ID의 카테고리를 조회
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    CategoryDTO findById(Long categoryId);

    /**
     * 새로운 카테고리를 추가
     * @param categoryDTO 추가할 카테고리 정보
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 카테고리 정보를 수정
     * @param categoryDTO 수정할 카테고리 정보
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 카테고리를 삭제
     * @param categoryId 삭제할 카테고리 ID
     */
    void delete(Long categoryId);
}