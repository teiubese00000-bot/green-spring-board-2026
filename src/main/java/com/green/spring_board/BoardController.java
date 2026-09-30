package com.green.spring_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

// 전체조회
    @GetMapping
    public List<Boards> getBoards() {
        return boardRepository.findAll();
    }

    /*  @GetMapping
      public String getTitle() {
          Boards board = boardRepository.findById(1).get();
          return board.getTitle();
      }

     */
// 외부값 읽기와 저장
    @PostMapping
    public void createBoard(@RequestBody BoardCreateRequest boardCreateRequest) {
        System.out.println(boardCreateRequest.getTitle());
        System.out.println(boardCreateRequest.getContent());

//저장
        Boards board =new Boards();
        board.setTitle(boardCreateRequest.getTitle());
        board.setContent(boardCreateRequest.getContent());

        boardRepository.save(board);

    }


// 수정


// 삭제


}