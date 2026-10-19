package com.green.spring_board.scheduler;

import com.green.spring_board.repository.AccessTokenBlacklistRepository;
import com.green.spring_board.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j

public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;
    private  final AccessTokenBlacklistRepository accessTokenBlacklistRepository;

    //초 분 시 일 월 요일
    // 매 단위에 실행할 것이다 -> *
    // 특정 값에 실행할 것이다 -> 1, 2, 3, 등 상수
    // 특정 값 기준 일정 주기로 실행할 것이다 -> 기준점/ 주기(예. 0/10)

    // 10 * * * * * -> 매분의 10초마다 실행(1분에 한번)
    // 0/10 * * * * * -> 10초마다 실행

    // 매일 오전 3시 만료된 불랙리스트의 액세스토큰과 만료된 리프레시 토큰 삭제하는 스케줄러
    @Transactional  // 원자성 보장하는 작업에 붙인다
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public  void cleanTokens(){
        LocalDateTime now = LocalDateTime.now();
        log.warn("토큰 정리 스케줄러 실행. 실행시작시간: {}", now);

        // 1. 리프레시 테이블에서 만료 시간 지난 리프레시  찾아 제거
        refreshTokenRepository.deleteAllByExpirationDateTimeBefore(now);

        // 2. 액세스 토큰 블랙리스트 테이블에서 만료시간 지난 액세스 토큰 제거
        accessTokenBlacklistRepository.deleteAllByExpirationDateTimeBefore(now);

        log.warn("토큰정리 스케줄러가 작업이 성공적으로 마무리");

    }


}
