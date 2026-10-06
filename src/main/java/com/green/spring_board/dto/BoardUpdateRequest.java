package com.green.spring_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BoardUpdateRequest {
    //board 수정의 경우 수정하려는 필드 값만 요청에 담아 보낸다


    @NotBlank
    @Size(min = 10, max = 50)  // 10자 이상, 50자 이하
    private String title;

    @NotBlank
    @Size(min =10) // 10자 이상
    private String content;
}

