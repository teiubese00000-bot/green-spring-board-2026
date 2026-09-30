package com.green.spring_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BoardRepository extends JpaRepository<Boards,Integer> { //보드 테이블 기준으로 테이블만들어라


}
