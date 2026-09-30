package com.green.spring_board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test") // 이 주소로 들어오는 요청은 하단에서 처리한다
public class TestController {
    @GetMapping
    public String test(){
        return "hello world!";
    }
}
