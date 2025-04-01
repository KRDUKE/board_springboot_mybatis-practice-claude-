package com.dukefirstboard.board.service;

import com.dukefirstboard.board.dto.BoardDTO;
import com.dukefirstboard.board.dto.BoardFileDTO;
import com.dukefirstboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

// @Service: 이 클래스가 Spring의 서비스 계층임을 나타냄.
// 비즈니스 로직을 처리하며, 컨트롤러와 리포지토리 사이를 연결.
@Service
// @RequiredArgsConstructor: Lombok 어노테이션으로, final 필드(boardRepository)에 대한 생성자를 자동 생성.
// 의존성 주입으로 BoardRepository를 받아옴.
@RequiredArgsConstructor
public class BoardService {
    // 데이터베이스 작업을 수행하는 BoardRepository 객체.
    // 이 객체를 통해 DB에 데이터를 저장, 조회, 수정, 삭제.
    private final BoardRepository boardRepository;

    // 게시글과 첨부 파일을 저장하는 메서드.
    // 매개변수: BoardDTO (게시글 데이터와 파일 리스트 포함).
    // 기능: 파일 유무를 확인하고, 파일이 있으면 서버에 저장 후 DB에 기록.
    public void save(BoardDTO boardDTO) throws IOException {
        // 첫 번째 파일이 비어 있는지 확인 (파일 업로드 여부 판단).
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            // 파일이 없는 경우.
            // fileAttached를 0으로 설정해 파일 첨부 없음을 표시.
            boardDTO.setFileAttached(0);
            // 게시글만 DB에 저장.
            boardRepository.save(boardDTO);
        } else {
            // 파일이 있는 경우.
            // fileAttached를 1로 설정해 파일 첨부 있음을 표시.
            boardDTO.setFileAttached(1);
            // 게시글을 먼저 저장하고, 생성된 id를 받음 (파일과 연결하기 위해).
            BoardDTO savedBoard = boardRepository.save(boardDTO);
            // 업로드된 파일 리스트를 순회하며 처리.
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                // 원본 파일 이름 가져오기 (사용자가 업로드한 파일명).
                String originalFilename = boardFile.getOriginalFilename();
                // 디버깅용으로 원본 파일명 출력.
                System.out.println("originalFilename = " + originalFilename);
                // 저장용 파일 이름 생성 (중복 방지를 위해 타임스탬프 추가).
                System.out.println(System.currentTimeMillis());
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                System.out.println("storedFileName = " + storedFileName);
                // BoardFileDTO 객체 생성 및 값 설정.
                BoardFileDTO boardFileDTO = new BoardFileDTO();
                boardFileDTO.setOriginalFileName(originalFilename); // 원본 파일명.
                boardFileDTO.setStoredFileName(storedFileName);     // 저장용 파일명.
                boardFileDTO.setBoardId(savedBoard.getId());        // 연결된 게시글 ID.
                // 파일을 저장할 경로 지정.
                String savePath = "C:/Users/DUKE/Downloads/testsave/" + storedFileName; // 현재 경로.
                // 주석 처리된 경로는 이전에 사용했던 경로.
//            String savePath = "C:/development/intellij_community/spring_upload_files/" + storedFileName;
                // 파일을 지정된 경로에 저장.
                boardFile.transferTo(new File(savePath));
                // 파일 정보를 DB에 저장 (board_file_table).
                boardRepository.saveFile(boardFileDTO);
            }
        }
    }

    // 모든 게시글 목록을 조회하는 메서드.
    // 반환: List<BoardDTO> (모든 게시글 데이터).
    public List<BoardDTO> findAll() {
        // BoardRepository의 findAll 메서드를 호출해 목록을 가져옴.
        return boardRepository.findAll();
    }

    // 게시글 조회수를 증가시키는 메서드.
    // 매개변수: id (조회수를 증가시킬 게시글 ID).
    public void updateHits(Long id) {
        // BoardRepository의 updateHits 메서드를 호출해 조회수 업데이트.
        boardRepository.updateHits(id);
    }

    // 특정 게시글의 상세 정보를 조회하는 메서드.
    // 매개변수: id (조회할 게시글 ID).
    // 반환: BoardDTO (해당 게시글 데이터).
    public BoardDTO findById(Long id) {
        // BoardRepository의 findById 메서드를 호출해 데이터 조회.
        return boardRepository.findById(id);
    }

    // 게시글을 수정하는 메서드.
    // 매개변수: BoardDTO (수정할 게시글 데이터).
    public void update(BoardDTO boardDTO) {
        // BoardRepository의 update 메서드를 호출해 데이터 수정.
        boardRepository.update(boardDTO);
    }

    // 게시글을 삭제하는 메서드.
    // 매개변수: id (삭제할 게시글 ID).
    public void delete(Long id) {
        // BoardRepository의 delete 메서드를 호출해 게시글 삭제.
        boardRepository.delete(id);
    }

    // 특정 게시글에 첨부된 파일 목록을 조회하는 메서드.
    // 매개변수: id (게시글 ID).
    // 반환: List<BoardFileDTO> (해당 게시글의 파일 데이터 목록).
    public List<BoardFileDTO> findFile(Long id) {
        // BoardRepository의 findFile 메서드를 호출해 파일 목록 조회.
        return boardRepository.findFile(id);
    }
}