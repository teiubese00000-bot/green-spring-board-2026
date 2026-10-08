package com.green.spring_board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity  // DB 엔티티
@Table(name="comments") // 테이블연결



public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String  content;


    @ManyToOne(fetch = FetchType.LAZY) // DB 다대일
    @JoinColumn(name="user_id", nullable = false)  // 조인
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)  // DB 다대일
    @JoinColumn(name="board_id", nullable = false)  // 조인
    private Board board;

    @Column(nullable = false, name="created_datetime",insertable = false, updatable = false)
    private LocalDateTime createdDatetime;

    @Column(nullable = false)
    private boolean isDeleted;
}
