package com.green.spring_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController   // HTTP 요청을 받고, 반환값을 JSON 응답으로 전달하는 컨트롤러
@RequestMapping("/api/board")  // 이 컨트롤러의 공통 URL
public class BoardController {
     // 게시글 DB에 접근하는 객체
    private BoardRepository boardRepository;
    
     // 생성자 주입: Spring이 BoardRepository를 전달해 줌
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

// 전체조회 // GET /api/board → 전체 게시글 조회
    @GetMapping
    public List<Boards> getBoards() {
         // DB에서 전체 게시글을 조회해 반환
        return boardRepository.findAll();
    }

    /*  @GetMapping
      public String getTitle() {
          Boards board = boardRepository.findById(1).get();
          return board.getTitle();
      }
     */

    
// 외부값 읽기와 저장 -삽입
    @PostMapping  // POST /api/board → 게시글 작성
    public void createBoard(@RequestBody BoardCreateRequest boardCreateRequest) {
        // 요청 JSON을 BoardCreateRequest 객체로 받아 값을 확인
        System.out.println(boardCreateRequest.getTitle());
        System.out.println(boardCreateRequest.getContent());

//저장  // DB에 저장할 게시글 엔티티 생성
        Boards board = new Boards();
        
         // 요청 객체의 제목과 내용을 엔티티에 복사
        board.setTitle(boardCreateRequest.getTitle());
        board.setContent(boardCreateRequest.getContent());

         // 새 게시글을 DB에 저장
        boardRepository.save(board);

    }


// 수정


// 삭제


}
