package com.green.spring_board.entity;

import com.green.spring_board.global.UserState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="created_date_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdDatetime;

    @Column(name ="update_date_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedDatetime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)  /// 값을 가져와서 스트링아닌 이넘으로 변경해서 넘겨준
    private UserState state;
}