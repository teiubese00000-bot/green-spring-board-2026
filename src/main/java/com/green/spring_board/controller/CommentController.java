package com.green.spring_board.controller;

import com.green.spring_board.dto.ApiResponse;
import com.green.spring_board.dto.CommentCreateRequest;
import com.green.spring_board.dto.CommentResponse;
import com.green.spring_board.dto.UpdateCommentRequest;
import com.green.spring_board.exceptions.UnauthenticatedException;
import com.green.spring_board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController   // api 요청 받는 클래스
@RequestMapping("/api")   // 댓글 api의 기본주소 지정
@RequiredArgsConstructor   // 서비스를 주입받을 생성자 자동생성
public class CommentController {
    private  final CommentService commentService;

    @PostMapping("/board/{id}/comment")  //url
    public ResponseEntity<ApiResponse<Void>> createComment(
            @PathVariable int id,
            @RequestBody CommentCreateRequest commentCreateRequest,
            HttpServletRequest httpServletRequest)
    {
        HttpSession session= httpServletRequest.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        int userId = (int) session.getAttribute("userId");

        commentService.createComment(commentCreateRequest, userId, id);

        return ResponseEntity.ok(ApiResponse.ok());
    }
    @GetMapping("/board/{id}/comment")  //url
    public ResponseEntity<ApiResponse<List<CommentResponse>>> readComments(
            @PathVariable int id

    ){
    return  ResponseEntity.ok(
            ApiResponse.ok(commentService.readComments(id))
    );

    }

    @PatchMapping("/comment/{id}")
    public ResponseEntity<ApiResponse<Void>>updateComment(
            @PathVariable int id, @Valid @RequestBody UpdateCommentRequest updateCommentRequest,
            HttpServletRequest httpServletRequest){
        HttpSession session= httpServletRequest.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        int userId = (int) session.getAttribute("userId");

        commentService.updateComment(id, updateCommentRequest, userId);


        //코드 미완성
        return ResponseEntity.ok(ApiResponse.ok());

    }
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable int id,HttpServletRequest httpServletRequest){
        HttpSession session= httpServletRequest.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        // 아이디 꺼낸다
        int userId = (int) session.getAttribute("userId");

        commentService.deleteComment(id,userId);


        //코드 미완성
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
