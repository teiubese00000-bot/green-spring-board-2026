package com.green.spring_board.repository;

import com.green.spring_board.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends
        JpaRepository<RefreshToken, Integer> {

    List<RefreshToken> findByUserId(int id);
    void deleteByUserId(int userId);
    Optional<RefreshToken> findByToken(String token);

    // ExpirationDateTime이 매개변수의 시간보다 이전인 모든 데이터 삭제
    void deleteAllByExpirationDateTimeBefore(LocalDateTime dateTime);

}
