package com.green.spring_board;

// 게시글 작성 요청의 JSON 데이터를 받는 객체
public class BoardCreateRequest {
    private String title;     // 요청으로 받은 제목
    private String content;   // 요청으로 받은 내용

  // 제목과 내용을 전달해서 객체를 만드는 생성자
    public BoardCreateRequest(String title, String content){
        this.title = title;
        this.content = content;
    }
    
 // JSON을 객체로 변환할 때 사용할 수 있는 기본 생성자
    public BoardCreateRequest(){}

 // 제목 읽기
    public String getTitle() {
        return title;
    }

     // 제목 설정
    public void setTitle(String title) {
        this.title = title;
    }

     // 내용 읽기
    public String getContent() {
        return content;
    }

     // 내용 설정
    public void setContent(String content) {
        this.content = content;
    }
}
