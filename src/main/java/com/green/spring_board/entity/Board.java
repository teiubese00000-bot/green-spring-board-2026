package com.green.spring_board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Entity // JPA가 관리할 클래스로 지정
@Table(name="boards")
public class Board {
    @Id  //GeneratedValue 자동
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String content;

    @Column(nullable = false)
    private  int hits;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateDateTime;
}
