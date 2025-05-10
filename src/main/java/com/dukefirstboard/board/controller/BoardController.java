package com.dukefirstboard.board.controller;

import com.dukefirstboard.board.dto.*;
import com.dukefirstboard.board.service.BoardService;
import com.dukefirstboard.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    // 로거 추가 - 디버깅용
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    // BoardService 및 CategoryService 의존성 주입
    private final BoardService boardService;
    private final CategoryService categoryService;

    /**
     * 게시글 작성 폼을 표시할 때 카테고리 목록도 함께 전달
     */
    @GetMapping("/save")
    public String save(Model model) {
        logger.debug("게시글 작성 폼 요청");
        // 카테고리 목록을 모델에 추가
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "save";
    }

    /**
     * 게시글을 저장하는 메서드
     */
    @PostMapping("/save")
    public String save(BoardDTO boardDTO) throws IOException {
        logger.debug("게시글 저장 요청: {}", boardDTO);
        boardService.save(boardDTO);
        logger.info("게시글 저장 완료: id={}", boardDTO.getId());
        return "redirect:/board/list";
    }

    /**
     * 모든 게시글 목록을 페이징 처리하여 조회
     */
    @GetMapping("/list")
    public String findAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        logger.debug("게시글 목록 페이징 요청: page={}, size={}", page, size);

        // 페이지 정보 객체 생성
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);

        // 페이징 처리된 게시글 목록 조회
        List<BoardDTO> boardDTOList = boardService.findAllWithPaging(pageDTO);
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("page", pageDTO);

        // 카테고리 목록을 모델에 추가 (네비게이션용)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "list";
    }

    /**
     * 카테고리별 게시글 목록을 페이징 처리하여 조회
     */
    @GetMapping("/category/{categoryId}")
    public String findByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        logger.debug("카테고리별 게시글 목록 페이징 요청: categoryId={}, page={}, size={}",
                categoryId, page, size);

        // 페이지 정보 객체 생성
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);

        // 페이징 처리된 카테고리별 게시글 목록 조회
        List<BoardDTO> boardDTOList = boardService.findByCategoryWithPaging(categoryId, pageDTO);
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("page", pageDTO);

        // 현재 선택된 카테고리 정보 전달
        CategoryDTO category = categoryService.findById(categoryId);
        model.addAttribute("category", category);

        // 모든 카테고리 목록도 함께 전달 (네비게이션용)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "list";
    }

    /**
     * 특정 게시글의 상세 내용을 조회
     */
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

        if (boardDTO.getFileAttached() == 1) {
            List<BoardFileDTO> boardFileDTOList = boardService.findFile(id);
            model.addAttribute("boardFileList", boardFileDTOList);
        }
        return "detail";
    }

    /**
     * 게시글 수정 폼을 보여주는 메서드
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

        // 카테고리 목록을 모델에 추가 (수정 폼에서 선택 가능하도록)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "update";
    }

    /**
     * 게시글을 수정하는 메서드
     */
    @PostMapping("/update/{id}")
    public String update(BoardDTO boardDTO, Model model) {
        logger.debug("게시글 수정 요청: {}", boardDTO);
        boardService.update(boardDTO);
        BoardDTO dto = boardService.findById(boardDTO.getId());
        model.addAttribute("board", dto);
        return "detail";
    }

    /**
     * 게시글을 삭제하는 메서드
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        logger.debug("게시글 삭제 요청: id={}", id);
        boardService.delete(id);
        return "redirect:/board/list";
    }

    /**
     * 게시글 검색 기능 (페이징 처리)
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        logger.debug("게시글 검색 페이징 요청: searchType={}, keyword={}, categoryId={}, page={}, size={}",
                searchType, searchKeyword, categoryId, page, size);

        // 검색 조건 객체 생성
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setCategoryId(categoryId);

        // 페이지 정보 객체 생성
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);

        // 페이징 처리된 검색 결과 조회
        List<BoardDTO> boardDTOList = boardService.searchWithPaging(searchDTO, pageDTO);
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("page", pageDTO);

        // 검색 조건을 모델에 추가 (폼에 검색어 유지)
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("selectedCategoryId", categoryId);

        // 카테고리 목록을 모델에 추가 (네비게이션용)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "list";
    }
}