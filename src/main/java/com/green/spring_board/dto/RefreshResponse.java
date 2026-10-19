package com.green.spring_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RefreshResponse {

    private  String  accessToken;

    public RefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
