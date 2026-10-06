package com.green.spring_board.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {
    @Email
    @Size(max=100)
    private String email;

    @Size(min=1, max=30)
    private String nickname;

    // 값 유무 검증

    // @NotNull - null 은 허용하지 않는디(빈문자열 허용)
    // @NotEmpty 문자열 or콜렉션이 비어있으면 안된다 (공백으로 채운 문자열 허용)
    // @NotBlank - notnull && not empty && 공백으로 채운 문자열 허용X

    //값 범위 검증
    // @Size(min, max) 문자열 or콜렉션의 최소길이, 최대길이 검사
    // Size는 길이만 검사. 숫자값의 범위 검사용도는 아니다\
    // @Min, @Max 숫자값의 최소값, 최대값 범위 검사
    // @Positive, @Negative 숫자값이 양수인지 음수인지 검사
    // @Post, @Future 날짜가 과거인지 미래인지 검사

    // 값 형태 검증
    // // @Email  메일 형식이지 검사

}
