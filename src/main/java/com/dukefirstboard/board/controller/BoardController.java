package com.dukefirstboard.board.controller;

import com.dukefirstboard.board.dto.BoardDTO;
import com.dukefirstboard.board.dto.BoardFileDTO;
import com.dukefirstboard.board.dto.CategoryDTO;
import com.dukefirstboard.board.dto.SearchDTO;
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
     * 카테고리별 게시글 목록을 조회
     */
    @GetMapping("/category/{categoryId}")
    public String findByCategory(@PathVariable Long categoryId, Model model) {
        logger.debug("카테고리별 게시글 목록 요청: categoryId={}", categoryId);
        List<BoardDTO> boardDTOList = boardService.findByCategory(categoryId);
        model.addAttribute("boardList", boardDTOList);

        // 현재 선택된 카테고리 정보 전달
        CategoryDTO category = categoryService.findById(categoryId);
        model.addAttribute("category", category);

        // 모든 카테고리 목록도 함께 전달 (네비게이션용)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "list";
    }

    /**
     * 모든 게시글 목록을 조회할 때 카테고리 목록도 함께 전달
     */
    @GetMapping("/list")
    public String findAll(Model model) {
        logger.debug("게시글 목록 요청");
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);

        // 카테고리 목록을 모델에 추가 (네비게이션용)
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
     * 게시글 검색 기능
     * @param searchType 검색 유형 (title, content, writer, titleAndContent)
     * @param searchKeyword 검색 키워드
     * @param categoryId 카테고리 ID (선택적)
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 검색 결과 페이지
     */
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String searchType,
                         @RequestParam(required = false) String searchKeyword,
                         @RequestParam(required = false) Long categoryId,
                         Model model) {
        logger.debug("게시글 검색 요청: searchType={}, keyword={}, categoryId={}",
                searchType, searchKeyword, categoryId);

        // 검색 조건 객체 생성
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setCategoryId(categoryId);

        // 검색 실행
        List<BoardDTO> boardDTOList = boardService.search(searchDTO);
        model.addAttribute("boardList", boardDTOList);

        // 검색 조건을 모델에 추가 (폼에 검색어 유지)
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("selectedCategoryId", categoryId);

        // 카테고리 목록을 모델에 추가 (네비게이션용)
        List<CategoryDTO> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        // 검색 결과 수를 모델에 추가
        model.addAttribute("resultCount", boardDTOList.size());

        return "list";  // 목록 페이지 재사용
    }
}