package com.dukefirstboard.board.controller;

import com.dukefirstboard.board.dto.CategoryDTO;
import com.dukefirstboard.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 카테고리 관련 HTTP 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    // 로거 추가 - 디버깅용
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // CategoryService 의존성 주입
    private final CategoryService categoryService;

    /**
     * 카테고리 관리 페이지를 표시
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 카테고리 관리 페이지 뷰 이름
     */
    @GetMapping("/manage")
    public String manage(Model model) {
        logger.debug("카테고리 관리 페이지 요청");
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "category/manage";
    }

    /**
     * 새 카테고리를 추가
     * @param categoryDTO 추가할 카테고리 정보
     * @return 카테고리 관리 페이지로 리다이렉트
     */
    @PostMapping("/save")
    public String save(@ModelAttribute CategoryDTO categoryDTO) {
        logger.debug("카테고리 저장 요청: {}", categoryDTO);
        categoryService.save(categoryDTO);
        return "redirect:/category/manage";
    }

    /**
     * 카테고리 정보를 수정
     * @param categoryDTO 수정할 카테고리 정보
     * @return 카테고리 관리 페이지로 리다이렉트
     */
    @PostMapping("/update")
    public String update(@ModelAttribute CategoryDTO categoryDTO) {
        logger.debug("카테고리 수정 요청: {}", categoryDTO);
        categoryService.update(categoryDTO);
        return "redirect:/category/manage";
    }

    /**
     * 카테고리를 삭제
     * @param categoryId 삭제할 카테고리 ID
     * @return 카테고리 관리 페이지로 리다이렉트
     */
    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable Long categoryId) {
        logger.debug("카테고리 삭제 요청: id={}", categoryId);
        categoryService.delete(categoryId);
        return "redirect:/category/manage";
    }
}