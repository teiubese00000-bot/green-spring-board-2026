package com.green.spring_board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/board")
@AllArgsConstructor
public class BoardController {
    private BoardRepository boardRepository;


// 전체조회
    @GetMapping
    public List<Boards> getBoards() {
        return boardRepository.findAll();
    }

    /*
     전체조회
     @GetMapping
      public String getTitle() {
          Boards board = boardRepository.findById(1).get();
          return board.getTitle();
      }*/

// 상세조회
    @GetMapping("/{id}")
    public Boards getBoardDetail(@PathVariable int id){
        Boards board= boardRepository.findById(id).get();
        board.setHits(board.getHits()+1);
        boardRepository.save(board);

        return board;

    }



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
@PatchMapping("/{id}")
public void updateBoard(
        @PathVariable int id,
        @RequestBody BoardCreateRequest boardCreateRequest) {
//
    Boards board = boardRepository.findById(id).get();

    if (boardCreateRequest.getTitle() != null) {
        board.setTitle(boardCreateRequest.getTitle());
    }

    if (boardCreateRequest.getContent() != null) {
        board.setContent(boardCreateRequest.getContent());
    }
    boardRepository.save(board);
}


// 삭제
@DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable int id){
      boardRepository.deleteById(id);
}

}