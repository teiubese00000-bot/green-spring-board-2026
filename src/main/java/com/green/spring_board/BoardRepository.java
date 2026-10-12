package com.green.spring_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

  // 보드 테이블 기준으로 테이블 만들어라 -> 보드 컨트롤러
public interface BoardRepository extends JpaRepository<Boards,Integer> { 


}
