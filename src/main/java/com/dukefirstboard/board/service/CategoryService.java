package com.dukefirstboard.board.service;

import com.dukefirstboard.board.dto.CategoryDTO;
import com.dukefirstboard.board.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    // 로거 추가 - 디버깅용
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    // CategoryMapper 의존성 주입
    private final CategoryMapper categoryMapper;

    /**
     * 모든 카테고리 목록을 조회
     * @return 카테고리 목록
     */
    public List<CategoryDTO> findAll() {
        logger.debug("카테고리 목록 조회");
        return categoryMapper.findAll();
    }

    /**
     * 특정 ID의 카테고리를 조회
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    public CategoryDTO findById(Long categoryId) {
        logger.debug("카테고리 조회: id={}", categoryId);
        return categoryMapper.findById(categoryId);
    }

    /**
     * 새로운 카테고리를 추가
     * @param categoryDTO 추가할 카테고리 정보
     */
    public void save(CategoryDTO categoryDTO) {
        logger.debug("카테고리 저장: {}", categoryDTO);
        categoryMapper.save(categoryDTO);
    }

    /**
     * 카테고리 정보를 수정
     * @param categoryDTO 수정할 카테고리 정보
     */
    public void update(CategoryDTO categoryDTO) {
        logger.debug("카테고리 수정: {}", categoryDTO);
        categoryMapper.update(categoryDTO);
    }

    /**
     * 카테고리를 삭제
     * @param categoryId 삭제할 카테고리 ID
     */
    public void delete(Long categoryId) {
        logger.debug("카테고리 삭제: id={}", categoryId);
        categoryMapper.delete(categoryId);
    }
}