package com.green.spring_board.exceptions;



// 누군지 알지만(인증은 되엇으나) 해당작업을 허용하지 않음(403)
public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String message) {
        super(message);
    }
}
