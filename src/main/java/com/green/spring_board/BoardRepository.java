package com.green.spring_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// DB에 접근하는 Repository임을 표시
@Repository

   // JpaRepository를 상속해서 기본 조회·저장·삭제 메서드를 사용 -> 보드 컨트롤러
public interface BoardRepository extends JpaRepository<Boards,Integer> { 


}
