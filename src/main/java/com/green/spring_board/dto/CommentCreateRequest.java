package com.green.spring_board.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


public class CommentCreateRequest {
    @NotBlank // 비어 있거나 공백인 요청을 막음
    private String content;
}
