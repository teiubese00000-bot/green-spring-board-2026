package com.green.spring_board.controller;

import com.green.spring_board.dto.LoginRequest;
import com.green.spring_board.dto.MyInfoResponse;
import com.green.spring_board.dto.SignupRequest;
import com.green.spring_board.dto.UserUpdateRequest;
import com.green.spring_board.exceptions.UnauthenticatedException;
import com.green.spring_board.repository.UserRepository;
import com.green.spring_board.service.BoardService;
import com.green.spring_board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final BoardService boardService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid@RequestBody SignupRequest signupRequest) {

            userService.signup(signupRequest);
            return ResponseEntity.ok().build();

    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest loginRequest,
            // 잘못된 입력을 검사하려고 @Valid와 @NotBlank를 쓰고, 검사에 실패하면 보통 400 응답
            // @NotBlank: “이 문자열은 null, 빈 문자열, 공백만 있는 값이면 안 돼
            // @Valid: “DTO에 적힌 조건을 검사해 줘
            HttpServletRequest httpServletRequest
    ){
        // DTO


            int userId = userService.login(loginRequest);
            HttpSession session = httpServletRequest.getSession();
            httpServletRequest.changeSessionId();
            session.setAttribute("userId", userId);
            return ResponseEntity.ok().build();

    }

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getCurrentUser(
            HttpServletRequest httpServletRequest
    ){
        // 1. 이 사람의 세션을 가져옴
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인 필요");
        }

        // 2. 세션에서 유저 아이디 뽑아옴
        int userId = (int) session.getAttribute("userId");
        MyInfoResponse response = userService.getUserInfo(userId);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }

        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateUserInfo(
            HttpServletRequest request,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest
    ){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인 필요");
        }
        int userId = (int) session.getAttribute("userId");
        userService.updateUserInfo(userId, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    // 유저 탈퇴 기능
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("userId") == null) {
            throw new UnauthenticatedException("로그인 필요");
        }
        int userId = (int) session.getAttribute("userId");

        // 1. DB 삭제
        userService.deleteUser(userId);
        // 2. 세션 비활성화
        session.invalidate();

        return ResponseEntity.noContent().build();
    }
}