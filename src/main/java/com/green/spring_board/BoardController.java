package com.green.spring_board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/board")
@AllArgsConstructor
public class BoardController {
    private BoardRepository boardRepository;


// 전체조회
    @GetMapping
    public ResponseEntity<List<Boards>> getBoards() {
        return ResponseEntity.ok(boardRepository.findAll()  //
        );
     //   return boardRepository.findAll(); -데이터만
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
    public ResponseEntity <Boards> getBoardDetail(@PathVariable int id){
        Optional<Boards> optionalboard= boardRepository.findById(id);
        if (optionalboard.isEmpty()){
            //요청한 게시글 번호를 찾지 못한경우
            return ResponseEntity.notFound().build();
        }
        Boards board = optionalboard.get();
        board.setHits(board.getHits()+1);
        boardRepository.save(board);

        return ResponseEntity.ok(board);

    }


// 삽입 ///////////////
// 외부값 읽기
    @PostMapping
    public ResponseEntity<Boards> createBoard(@RequestBody BoardCreateRequest boardCreateRequest) {
        if (boardCreateRequest.getTitle() == null || boardCreateRequest.getTitle().isBlank()){
            return ResponseEntity.badRequest().build();

        }
        if (boardCreateRequest.getContent() == null || boardCreateRequest.getContent().isBlank()){
            return ResponseEntity.badRequest().build();
        }

        System.out.println(boardCreateRequest.getTitle());
        System.out.println(boardCreateRequest.getContent());

//저장
        Boards board = new Boards();
        board.setTitle(boardCreateRequest.getTitle());
        board.setContent(boardCreateRequest.getContent());

        Boards savedBoard = boardRepository.save(board); //Db에 저장되는 번호를 알아냄
        int newBoardId = savedBoard.getId();
        URI location = URI.create("/api/board/"+newBoardId);
        return ResponseEntity.created(location).body(board);
    }
/*
    public ResponseEntity <Boards> getBoardDetail(@PathVariable int id){
        Optional<Boards> optionalboard= boardRepository.findById(id);
          */

// 수정
@PatchMapping("/{id}")
public ResponseEntity<Void> updateBoard(
        @PathVariable int id,
        @RequestBody BoardCreateRequest boardCreateRequest) {

    Optional<Boards> optionalboard = boardRepository.findById(id);

    if (boardCreateRequest.getTitle() != null && ! boardCreateRequest.getTitle().isBlank()) {
      return ResponseEntity.badRequest().build();
    }

    if (boardCreateRequest.getContent() != null && ! boardCreateRequest.getContent().isBlank()) {
        return ResponseEntity.badRequest().build();
    }

    Boards board = optionalboard.get();
    boardRepository.save(board);
    return ResponseEntity.ok().build();
}


// 삭제
@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable int id){
    boolean isExist = boardRepository.existsById(id);
    if (!isExist) {

        return ResponseEntity.notFound().build();

    }
        boardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}