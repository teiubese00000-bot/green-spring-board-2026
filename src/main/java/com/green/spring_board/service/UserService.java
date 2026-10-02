package com.green.spring_board.service;


import com.green.spring_board.dto.LoginRequest;
import com.green.spring_board.dto.MyInfoResponse;
import com.green.spring_board.dto.SignupRequest;
import com.green.spring_board.entity.User;
import com.green.spring_board.exceptions.ResourceConflictException;
import com.green.spring_board.exceptions.ResourceNotFoundException;
import com.green.spring_board.exceptions.UnauthenticatedException;
import com.green.spring_board.exceptions.UserRequestException;
import com.green.spring_board.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder
            = new BCryptPasswordEncoder();

    /// 해싱

    public void signup(SignupRequest signupRequest) {
        //유저네임과 비밀번호가 공백인지아닌지 확인
        if (signupRequest.getEmail().isBlank() || signupRequest.getPassword().isBlank()) {
            throw new UserRequestException("Email or password cannot be blank");
        }

        //메일이 사용중인지확인
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourceConflictException("Email already exists");

        }
//g 해싱알고리즘
        String hashedPassword = passwordEncoder.encode(
                signupRequest.getPassword());

        // DB save
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setNickname(signupRequest.getNickname());
        userRepository.save(user);

    }

    public int login(LoginRequest loginRequest) {
        //1 메일 존재하는지
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        }
        User user = userOptional.get();

        //2 비번 바른지...비번이 맞지 않으면
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthenticatedException("Wrong password");

        }

        return user.getId();

    }

    public MyInfoResponse getUserInfo(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        }
        User user = userOptional.get();

        //4 db에서 닉네임과 이메일 받아옴
        String email = user.getEmail();
        String nickname = user.getNickname();


        //3 유저아이디로 db조회

        //5 돌려줌
        MyInfoResponse myInfoResponse = new MyInfoResponse();
        myInfoResponse.setEmail(email);
        myInfoResponse.setNickname(nickname);
        return myInfoResponse;
    }

    //수정할 아이디 닉네임 전달
    public void updateUserInfo(int userId, MyInfoResponse myInfoResponse) {
        //db에서 유저 아이디와 같은 거 찾아서 Optional<User>라는 userOptional에 담아
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        }
        User user = userOptional.get(); // 유저는 기존의 정보
        user.setEmail(myInfoResponse.getEmail());
        user.setNickname(myInfoResponse.getNickname());
        userRepository.save(user);
    }

    public void deleteUser(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        }
        User user = userOptional.get();
        userRepository.delete(user);

    }


}
