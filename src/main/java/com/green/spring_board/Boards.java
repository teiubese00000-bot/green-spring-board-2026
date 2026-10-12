package com.green.spring_board;

import jakarta.persistence.*;


@Entity  // JPA가 DB와 연결해서 관리하는 엔티티
@Table(name="boards")  // 연결할 테이블 이름
    
public class Boards {
    @Id  // 게시글을 구분하는 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String content;

    // JPA가 객체를 생성할 때 필요한 기본 생성자
    public Boards(){}

     // 값을 전달받아 객체를 생성하는 생성자
    public Boards(int id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

// 아래 getter와 setter는 기존 코드 그대로
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
