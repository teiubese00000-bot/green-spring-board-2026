package com.green.spring_board.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private  final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header=request.getHeader("Authorization");

        // startsWith () 안의 문자열로 시작하면
        if (header != null && header.startsWith("Bearer")){
            String token =header.substring(7);


            // 토큰 위/변조 여부 검사

            if (tokenProvider.validateAccessToken(token)){
                //TODO 블랙리스트 여부확인
                String username = tokenProvider.getUsernameFromToken(token);
                UserDetails userDetails;

                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                catch (UsernameNotFoundException e){
                    authenticationEntryPoint.commence(request,response, e);
                    return;
                }
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }
        // 남은 필터 있으면 더 실행해라..다음 필터 또는 서블릿으로 요청과 응답을 전달
        filterChain.doFilter(request, response);

    }
}
