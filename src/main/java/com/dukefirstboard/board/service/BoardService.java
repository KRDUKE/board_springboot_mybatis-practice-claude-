package com.dukefirstboard.board.service;

import com.dukefirstboard.board.dto.BoardDTO;
import com.dukefirstboard.board.dto.BoardFileDTO;
import com.dukefirstboard.board.dto.PageDTO;
import com.dukefirstboard.board.dto.SearchDTO;
import com.dukefirstboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    // 데이터베이스 작업을 수행하는 BoardRepository 객체
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Value("${file.upload-dir}")
    private String fileUploadDir;

    /**
     * 게시글과 첨부 파일을 저장하는 메서드
     * @param boardDTO 저장할 게시글 정보
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public void save(BoardDTO boardDTO) throws IOException {
        // 비밀번호 암호화
        boardDTO.setBoardPass(passwordEncoder.encode(boardDTO.getBoardPass()));

        // 첫 번째 파일이 비어 있는지 확인 (파일 업로드 여부 판단)
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            // 파일이 없는 경우
            boardDTO.setFileAttached(0);
            // 게시글만 DB에 저장
            boardRepository.save(boardDTO);
        } else {
            // 파일이 있는 경우
            boardDTO.setFileAttached(1);
            // 게시글을 먼저 저장하고, 생성된 id를 받음 (파일과 연결하기 위해)
            BoardDTO savedBoard = boardRepository.save(boardDTO);

            // 업로드 디렉토리가 없으면 생성
            Path uploadPath = Paths.get(fileUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 업로드된 파일 리스트를 순회하며 처리
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                // 원본 파일 이름 가져오기 (사용자가 업로드한 파일명)
                String originalFilename = boardFile.getOriginalFilename();
                // 디버깅용으로 원본 파일명 출력
                logger.debug("originalFilename = {}", originalFilename);
                // 저장용 파일 이름 생성 (중복 방지를 위해 타임스탬프 추가)
                String storedFileName = System.currentTimeMillis() + "-" + originalFilename;
                logger.debug("storedFileName = {}", storedFileName);

                // BoardFileDTO 객체 생성 및 값 설정
                BoardFileDTO boardFileDTO = new BoardFileDTO();
                boardFileDTO.setOriginalFileName(originalFilename); // 원본 파일명
                boardFileDTO.setStoredFileName(storedFileName);     // 저장용 파일명
                boardFileDTO.setBoardId(savedBoard.getId());        // 연결된 게시글 ID

                // 파일을 저장할 경로 지정
                String savePath = fileUploadDir + storedFileName;
                // 파일을 지정된 경로에 저장
                boardFile.transferTo(new File(savePath));
                // 파일 정보를 DB에 저장 (board_file_table)
                boardRepository.saveFile(boardFileDTO);
            }
        }
    }

    /**
     * 모든 게시글 목록을 조회하는 메서드
     * @return 모든 게시글 목록
     */
    public List<BoardDTO> findAll() {
        return boardRepository.findAll();
    }

    /**
     * 게시글 조회수를 증가시키는 메서드
     * @param id 조회수를 증가시킬 게시글 ID
     */
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    /**
     * 특정 게시글의 상세 정보를 조회하는 메서드
     * @param id 조회할 게시글 ID
     * @return 게시글 정보
     */
    public BoardDTO findById(Long id) {
        return boardRepository.findById(id);
    }

    /**
     * 게시글을 수정하는 메서드
     * @param boardDTO 수정할 게시글 정보
     */
    public void update(BoardDTO boardDTO) {
        boardRepository.update(boardDTO);
    }

    /**
     * 게시글을 삭제하는 메서드
     * @param id 삭제할 게시글 ID
     */
    public void delete(Long id) {
        boardRepository.delete(id);
    }

    /**
     * 특정 게시글에 첨부된 파일 목록을 조회하는 메서드
     * @param id 게시글 ID
     * @return 첨부 파일 목록
     */
    public List<BoardFileDTO> findFile(Long id) {
        return boardRepository.findFile(id);
    }

    /**
     * 게시글 비밀번호를 확인하는 메서드
     * @param id 게시글 ID
     * @param password 입력된 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean verifyPassword(Long id, String password) {
        BoardDTO board = findById(id);
        return board != null && passwordEncoder.matches(password, board.getBoardPass());
    }

    /**
     * 카테고리별 게시글 목록을 조회하는 메서드
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 게시글 목록
     */
    public List<BoardDTO> findByCategory(Long categoryId) {
        logger.debug("카테고리별 게시글 목록 조회: categoryId={}", categoryId);
        return boardRepository.findByCategory(categoryId);
    }

    /**
     * 검색 조건에 따라 게시글을 검색하는 메서드
     * @param searchDTO 검색 조건 (검색 타입, 검색어, 카테고리 ID)
     * @return 검색 결과 게시글 목록
     */
    public List<BoardDTO> search(SearchDTO searchDTO) {
        logger.debug("게시글 검색: searchType={}, keyword={}, categoryId={}",
                searchDTO.getSearchType(), searchDTO.getSearchKeyword(), searchDTO.getCategoryId());
        return boardRepository.search(searchDTO);
    }

    /**
     * 게시글 목록을 페이징 처리하여 조회
     * @param pageDTO 페이지 정보
     * @return 페이징된 게시글 목록
     */
    public List<BoardDTO> findAllWithPaging(PageDTO pageDTO) {
        // 전체 게시글 수 조회
        long totalElements = boardRepository.countAll();
        pageDTO.setTotalElements(totalElements);
        pageDTO.calculatePageInfo();

        logger.debug("페이징 처리된 게시글 목록 조회: page={}, size={}, total={}",
                pageDTO.getPage(), pageDTO.getSize(), totalElements);

        return boardRepository.findAllWithPaging(pageDTO);
    }

    /**
     * 카테고리별 게시글 목록을 페이징 처리하여 조회
     * @param categoryId 카테고리 ID
     * @param pageDTO 페이지 정보
     * @return 페이징된 게시글 목록
     */
    public List<BoardDTO> findByCategoryWithPaging(Long categoryId, PageDTO pageDTO) {
        // 카테고리별 게시글 수 조회
        long totalElements = boardRepository.countByCategory(categoryId);
        pageDTO.setTotalElements(totalElements);
        pageDTO.calculatePageInfo();

        logger.debug("카테고리별 페이징 처리된 게시글 목록 조회: categoryId={}, page={}, size={}, total={}",
                categoryId, pageDTO.getPage(), pageDTO.getSize(), totalElements);

        return boardRepository.findByCategoryWithPaging(categoryId, pageDTO);
    }

    /**
     * 검색 조건에 따라 게시글을 페이징 처리하여 검색
     * @param searchDTO 검색 조건
     * @param pageDTO 페이지 정보
     * @return 페이징된 검색 결과 게시글 목록
     */
    public List<BoardDTO> searchWithPaging(SearchDTO searchDTO, PageDTO pageDTO) {
        // 검색 조건에 맞는 게시글 수 조회
        long totalElements = boardRepository.countBySearch(searchDTO);
        pageDTO.setTotalElements(totalElements);
        pageDTO.calculatePageInfo();

        logger.debug("검색 결과 페이징 처리: searchType={}, keyword={}, categoryId={}, page={}, size={}, total={}",
                searchDTO.getSearchType(), searchDTO.getSearchKeyword(), searchDTO.getCategoryId(),
                pageDTO.getPage(), pageDTO.getSize(), totalElements);

        return boardRepository.searchWithPaging(searchDTO, pageDTO);
    }
}