package com.green.spring_board.controller;

import com.green.spring_board.dto.ApiResponse;
import com.green.spring_board.dto.BoardCreateRequest;
import com.green.spring_board.dto.BoardResponse;
import com.green.spring_board.dto.BoardUpdateRequest;

import com.green.spring_board.entity.Board;
import com.green.spring_board.exceptions.UnauthenticatedException;

import com.green.spring_board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardResponse>>> getBoards(){
        return ResponseEntity.ok(
                ApiResponse.ok(boardService.getAllBoards())
        );
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardDetail(@PathVariable int id){

            BoardResponse board = boardService.getBoard(id);
        return ResponseEntity.ok(ApiResponse.ok(board));
    }

  ///// 내 게시글 조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Board>>> getMyBoards(HttpServletRequest request
    ){
        HttpSession session = request.getSession(false); // 잇는 세션을 가져와라


        if (session==null || session.getAttribute("userId")==null){
            throw new UnauthenticatedException("로그인필요");
        }
        int userId=(Integer) session.getAttribute("userId"); // 세션에 저장된 유저아이디 꺼내옴


        ///////////위 까지가 누가 접속했는지 확인

        List<Board> boards= boardService.getMyBoards(userId); //서비스에 내 게시글 찾아줘
        // List<Board> boards =boardService.getAllBoards(userId); 서비스에 이 회원의 게시글 찾아줘
        return ResponseEntity.ok( ApiResponse.ok(boards));

    }





    // 삽입
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBoard(
            @Valid @RequestBody BoardCreateRequest boardCreateRequest,
            HttpServletRequest httpServletRequest
    ) {
            HttpSession session = httpServletRequest.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
              throw new UnauthenticatedException("로그인 필요");
            }

            int userId = (int) session.getAttribute("userId");
            int newBoardId = boardService.createBoard(boardCreateRequest, userId);
            URI location = URI.create("/api/board/" + newBoardId);

            return ResponseEntity.created(location).body(ApiResponse.<Void>ok(null));

    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable int id,
            @Valid @RequestBody BoardUpdateRequest boardUpdateRequest,
            HttpServletRequest httpServletRequest
    ){
            HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인 필요");
        }
        boardService.updateBoard(id, boardUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<Void>ok(null));

    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity <ApiResponse<Void>> deleteBoard(@PathVariable int id,
            HttpServletRequest httpServletRequest){

        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인 필요");
        }
        //TO DO 18:: 본인확인
        int userId= (Integer) session.getAttribute("userId");
        // 둘중 어느 방법을 쓸지 속한 컨벤션에 따르기
        // 삭제 성공 시 응답 방법 1.
        // 200 + ApiResponse<Void>

        // 삭제 성공 시 응답 2.
        // 204(no content) + no body
        boardService.deleteBoard(id,userId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}