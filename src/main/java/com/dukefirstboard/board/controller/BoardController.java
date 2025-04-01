package com.dukefirstboard.board.controller;

import com.dukefirstboard.board.dto.BoardDTO;
import com.dukefirstboard.board.dto.BoardFileDTO;
import com.dukefirstboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

// @Controller: 이 클래스가 Spring MVC 컨트롤러임을 나타냄. HTTP 요청을 처리하고 뷰(HTML)를 반환.
@Controller
// @RequiredArgsConstructor: Lombok 어노테이션으로, final 필드(boardService)에 대한 생성자를 자동 생성.
@RequiredArgsConstructor
public class BoardController {
    // BoardService 객체를 의존성 주입으로 받아 사용. 컨트롤러가 서비스 계층과 소통하도록 연결.
    private final BoardService boardService;

    // 게시글 작성 폼을 보여주는 메서드.
    // URL: "/save" (GET 요청).
    // 반환: "save"라는 Thymeleaf 템플릿(save.html)을 렌더링.
    @GetMapping("/save")
    public String save() {
        return "save";
    }

    // 게시글을 저장하는 메서드.
    // URL: "/save" (POST 요청).
    // 매개변수: BoardDTO (폼에서 전송된 게시글 데이터).
    // 기능: BoardService를 통해 게시글을 저장하고, 목록 페이지로 리다이렉트.
    @PostMapping("/save")
    public String save(BoardDTO boardDTO) throws IOException {
        // 디버깅용으로 받은 BoardDTO를 콘솔에 출력.
        System.out.println("boardDTO = " + boardDTO);
        // BoardService의 save 메서드를 호출해 게시글을 저장(파일 포함 가능).
        boardService.save(boardDTO);
        // 저장 후 게시글 목록 페이지로 리다이렉트.
        return "redirect:/list";
    }

    // 모든 게시글 목록을 조회하는 메서드.
    // URL: "/list" (GET 요청).
    // 매개변수: Model (뷰에 데이터를 전달하기 위한 객체).
    // 기능: BoardService에서 모든 게시글을 가져와 list.html에 표시.
    @GetMapping("/list")
    public String findAll(Model model) {
        // BoardService를 통해 모든 게시글 목록을 가져옴.
        List<BoardDTO> boardDTOList = boardService.findAll();
        // 뷰에 "boardList"라는 이름으로 게시글 목록을 전달.
        model.addAttribute("boardList", boardDTOList);
        // 디버깅용으로 목록을 콘솔에 출력.
        System.out.println("boardDTOList = " + boardDTOList);
        // list.html 템플릿을 렌더링.
        return "list";
    }

    // 특정 게시글의 상세 내용을 조회하는 메서드.
    // URL: "/{id}" (예: "/1", "/10") (GET 요청).
    // 매개변수: @PathVariable로 URL에서 id를 추출, Model로 뷰에 데이터 전달.
    // 기능: 조회수 증가 후 게시글 상세 정보와 첨부 파일을 가져와 detail.html에 표시.
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        // 조회수를 1 증가시킴.
        boardService.updateHits(id);
        // 해당 id의 게시글 상세 정보를 가져옴.
        BoardDTO boardDTO = boardService.findById(id);
        // 뷰에 "board"라는 이름으로 게시글 데이터를 전달.
        model.addAttribute("board", boardDTO);
        // 디버깅용으로 게시글 데이터를 콘솔에 출력.
        System.out.println("boardDTO = " + boardDTO);
        // 게시글에 파일이 첨부되어 있는지 확인 (fileAttached == 1).
        if (boardDTO.getFileAttached() == 1) {
            // 첨부 파일 목록을 가져옴.
            List<BoardFileDTO> boardFileDTOList = boardService.findFile(id);
            // 뷰에 "boardFileList"라는 이름으로 파일 목록을 전달.
            model.addAttribute("boardFileList", boardFileDTOList);
        }
        // detail.html 템플릿을 렌더링.
        return "detail";
    }

    // 게시글 수정 폼을 보여주는 메서드.
    // URL: "/update/{id}" (예: "/update/1") (GET 요청).
    // 매개변수: @PathVariable로 id 추출, Model로 뷰에 데이터 전달.
    // 기능: 수정할 게시글 데이터를 가져와 update.html에 표시.
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        // 해당 id의 게시글 데이터를 가져옴.
        BoardDTO boardDTO = boardService.findById(id);
        // 뷰에 "board"라는 이름으로 데이터를 전달.
        model.addAttribute("board", boardDTO);
        // update.html 템플릿을 렌더링.
        return "update";
    }

    // 게시글을 수정하는 메서드.
    // URL: "/update/{id}" (예: "/update/1") (POST 요청).
    // 매개변수: BoardDTO (수정된 데이터), Model로 뷰에 데이터 전달.
    // 기능: 수정된 데이터를 저장하고 상세 페이지로 이동.
    @PostMapping("/update/{id}")
    public String update(BoardDTO boardDTO, Model model) {
        // BoardService를 통해 수정된 데이터를 저장.
        boardService.update(boardDTO);
        // 수정 후 최신 데이터를 다시 가져옴.
        BoardDTO dto = boardService.findById(boardDTO.getId());
        // 뷰에 "board"라는 이름으로 최신 데이터를 전달.
        model.addAttribute("board", dto);
        // detail.html 템플릿을 렌더링.
        return "detail";
    }

    // 게시글을 삭제하는 메서드.
    // URL: "/delete/{id}" (예: "/delete/1") (GET 요청).
    // 매개변수: @PathVariable로 id 추출.
    // 기능: 게시글을 삭제하고 목록 페이지로 리다이렉트.
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        // BoardService를 통해 해당 id의 게시글을 삭제.
        boardService.delete(id);
        // 삭제 후 게시글 목록 페이지로 리다이렉트.
        return "redirect:/list";
    }
}