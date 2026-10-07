package com.green.spring_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApiResponse<T> {
    private  boolean success;
    private String message;
    private T data;

    // 생성자로 만들지 않고 굳이 ok, fail 등 정적 팩토리 메서드를 사용하는 이유
    // 사용하는 곳에서는  해당 클래스이 내부구조를 몰라도 된다
    // 생성자를 사용하는 경우 ApiResponse 구조가 수정되면 해당 쿨래스의 생성자 호출부 코드를 모두 바꿔줘야함

    // 성공  (데이터 O)
    // 빌더패턴의 장점
    // 생성자 오버로딩이 필요없음(줄일수 있다)
    // 객체 생성 코드만 봐도 어느 필드에 뭐가 있는지 알수있다(가독성좋음)
    public static <T> ApiResponse<T> ok(T data){
   //     return  new ApiResponse<>(true,data);

        return ApiResponse.<T>builder()
                .success(true) //필드를 메서드처럼 사용가능
                .data(data)
                .build();

    }
    // 성공  (데이터 X)
    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder()
                .success(true) // 필드를 메서드처럼 사용가능
                .build();

    }
    // 실패
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .success(false) //필드를 메서드처럼 사용가능
                .message(message)
                .build();

    }


}
