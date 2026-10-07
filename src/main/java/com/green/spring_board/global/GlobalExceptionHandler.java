package com.green.spring_board.global;
import java.util.Arrays;
import java.util.List;

import com.green.spring_board.dto.ApiResponse;
import com.green.spring_board.exceptions.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* 전역 예외 처리기
* 1. 어플리케이션 내 모든 컨트롤러에서 throw되는 예외를 이 곳에서 가로채 처리함
* 2. 예외처리 결과를 자동으로 JSON 응답 본문으로 변환
* 3. 개별 컨트롤러의 try-catch 코드 중복을 제거하고 클라이언트에게 일관된 에러 응답형식을 보장
* */

@RestControllerAdvice
@RestController
@Slf4j  //로깅시스템을 사용하도록 해줌
public class GlobalExceptionHandler {

    // 요청한 데이터를 찾을 수 없을 때 공통 처리
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException e){
        log.error(e.getMessage(), e);
        log.info("안녕");
        log.warn("경고");


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
    }

    //인증정보가 없거나 적절하지 않을때 공통처리
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthenticated(UnauthenticatedException e){
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail(e.getMessage()));
    }


    //validator등 입력 검증 과정에 문제 발생시 공통처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String >> handleValidationError(MethodArgumentNotValidException e){

        log.error(e.getMessage(), e);

        String  resultMessage="";
        List<FieldError> errors =e.getBindingResult().getFieldErrors();
        for (FieldError error : errors){
            resultMessage =resultMessage+error.getField()+"은(는)"+
                    error.getDefaultMessage()+ "\n";

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
              body(ApiResponse.fail(e.getMessage()));
    }

    // 고유값이 중복되어 저장실패 하거나 존재하지 않는 외래키를 사용해 데이터 생성 시도 등 문제상활 공통 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String >> handleDataConflict(DataIntegrityViolationException e){
        log.error(e.getMessage(), e);

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail("중복되거나 저장할수 없는 데이터"));
    }


    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<String >> handleConflict(ResourceConflictException e){
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.fail((e.getMessage())));

    }


    // 위의 것들과 달리 나머지들 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String >> handleException(Exception e){
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("서버에서 오류가 발생했습니다"));

    }

    // 권한이 없을 때 403 응답
    @ExceptionHandler(AuthorizationFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(AuthorizationFailureException e){
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail(e.getMessage()));
    }



// 현재 상태에서 수행할 수 없는 요청에 대한 공통 처리
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(InvalidStateException e){
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));

    }




}

