package com.green.spring_board.controller;

import com.green.spring_board.dto.LoginRequest;
import com.green.spring_board.dto.MyInfoResponse;
import com.green.spring_board.dto.SignupRequest;
import com.green.spring_board.exceptions.ResourceConflictException;
import com.green.spring_board.exceptions.ResourceNotFoundException;
import com.green.spring_board.exceptions.UnauthenticatedException;
import com.green.spring_board.exceptions.UserRequestException;
import com.green.spring_board.repository.UserRepository;
import com.green.spring_board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        try {
            userService.signup((signupRequest));
            return ResponseEntity.ok().build();

// 기존 가입자
        } catch (ResourceConflictException e) {
            return ResponseEntity.status(409).build();

        } catch (UserRequestException e) {
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Void> login
            (@RequestBody LoginRequest loginRequest,
             HttpServletRequest httpServletRequest) {
        try {
            int userId = userService.login(loginRequest);
            //세션
            HttpSession session = httpServletRequest.getSession();// 세션이 만들어짐
            httpServletRequest.changeSessionId();
            session.setAttribute("userId", userId);
            return ResponseEntity.ok().build();
            //유저 아이디,

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthenticatedException e) {
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //내정보 조회 세션은 왠만하면 컨트롤러가 다뤄야함. 요청과 응답에 대해서만 다룸
    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getCurrentUser
    (HttpServletRequest httpServletRequest) {

        //1. 이사람의 세션가져옴
        HttpSession session = httpServletRequest.getSession(false);
        // me는 회원 전용서비스라 세션이 없으면 올바른 접속자가 아니라서 꺼져..
        // 세션이 없으면 세션을 만들지 않도록 getSession(false) 한다

        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }

        //2 세션에서 유저 아이디 뽑아옴
        int userId = (int) session.getAttribute("userId");
        MyInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        session.invalidate();
        return ResponseEntity.ok().build();

    }

    // 메일 닉네임 수정
    @PatchMapping("/me")
    public ResponseEntity<Void> updateUserInfo(
            HttpServletRequest request, @RequestBody MyInfoResponse myInfoResponse) {// 세션가져옴
        HttpSession session = request.getSession(false);

        // 세션이나 로그인 정보 없으면 401
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }

        session = request.getSession(false);
        int userId = (int) session.getAttribute("userId");

        userService.updateUserInfo(userId, myInfoResponse);
        return ResponseEntity.ok().build();  //200


        //현재유저 가져와서 해당 정보를 사용자가 올린 요청으로 덮어씌운다
    }

    // 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        int userId = (int) session.getAttribute("userId");

        //1 DB삭제
        userService.deleteUser(userId);

        // 2 세션비활성화
        session.invalidate();

        return ResponseEntity.noContent().build();
    }

}
