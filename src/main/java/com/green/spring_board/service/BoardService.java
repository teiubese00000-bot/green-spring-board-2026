package com.green.spring_board.service;

import com.green.spring_board.dto.BoardCreateRequest;
import com.green.spring_board.dto.BoardResponse;
import com.green.spring_board.dto.BoardUpdateRequest;
import com.green.spring_board.entity.Like;
import com.green.spring_board.entity.User;
import com.green.spring_board.exceptions.AuthorizationFailureException;
import com.green.spring_board.exceptions.ResourceNotFoundException;
import com.green.spring_board.exceptions.UnauthenticatedException;

import com.green.spring_board.repository.BoardRepository;
import com.green.spring_board.entity.Board;
import com.green.spring_board.repository.LikeRepository;
import com.green.spring_board.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardService {
    private BoardRepository boardRepository;
    private UserRepository userRepository;
    private final LikeRepository likeRepository;

    // 전체 조회
    public List<BoardResponse> getAllBoards() {
        // List<Board> -> List<BoardResponse> 형태로 변환 후 반환
        List<Board> boards = boardRepository.findAll();

        // 1. List<BoardResponse> 형태의 빈 리스트 생성
        List<BoardResponse> boardResponses = new ArrayList<>();

        // 2. Board 개수만큼 반복하며 new BoardResponse 생성
        for (Board board : boards) {
            // 3. 1번에서 만든 리스트에 추가
            boardResponses.add(
                    new BoardResponse(
                            board.getId(),
                            board.getTitle(),
                            board.getContent(),
                            board.getHits(),
                            board.getUser().getId(),
                            board.getUser().getNickname(),
                            board.getCreatedDatetime(),
                            board.getUpdatedDatetime()
                    )
            );
        }
        return boardResponses;


    }

    // 상세 조회
    public BoardResponse getBoard(int id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isEmpty()) {
            // 요청한 게시글을 찾지 못한 경우
            throw new ResourceNotFoundException("요청한 게시글을 찾지 못했습니다.");
        }
        Board board = optionalBoard.get();

        User user = board.getUser();
        System.out.println(user.getNickname());
        board.setHits(board.getHits() + 1);
        boardRepository.save(board);
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getHits(),
                board.getUser().getId(),
                board.getUser().getNickname(),
                board.getCreatedDatetime(),
                board.getUpdatedDatetime()
        );
    }

    public int createBoard(BoardCreateRequest boardCreateRequest, Integer userId) {


        // userId 유효성 체크 (해당 userId의 유저가 정상적으로 존재하는지)
        // TODO :: 이후 삭제/탈퇴 유저에 대한 검증도 추가 필요
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UnauthenticatedException("로그인한 사용자를 찾을 수 없습니다.");
        }

        Board board = new Board();
        board.setTitle(boardCreateRequest.getTitle());
        board.setContent(boardCreateRequest.getContent());
        board.setUser(user.get());

        Board savedBoard = boardRepository.save(board);

        return savedBoard.getId();
    }

    public void updateBoard(int id, @Valid BoardUpdateRequest boardCreateRequest) {

        Optional<Board> optionalBoards = boardRepository.findById(id);
        if(optionalBoards.isEmpty()) {
            // 게시글을 못 찾은 경우
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Board board = optionalBoards.get();

        if(boardCreateRequest.getTitle() != null || ! boardCreateRequest.getTitle().isBlank()) {
            board.setTitle(boardCreateRequest.getTitle());
        }

        if(boardCreateRequest.getContent() != null && ! boardCreateRequest.getContent().isBlank()) {
            board.setContent(boardCreateRequest.getContent());
        }

        boardRepository.save(board);
    }

    public void deleteBoard(int id, int userId) {
        Optional<Board> optionalBoard= boardRepository.findById(id);

        if(optionalBoard.isEmpty()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }
        Board board =optionalBoard.get();
        if (board.getUser().getId() !=userId) {
            throw new AuthorizationFailureException("작업권한이 없음");

        }

        boardRepository.deleteById(id);
    }

    public List<Board> getMyBoards(int userId){
        return boardRepository.findAllByUser_Id(userId);
    }

    ///////// 좋아요
    public void pressLike(int id, int userId){
       Optional<Board> optionalBoard = boardRepository.findById(id);
       if (optionalBoard.isEmpty()){
           throw new ResourceNotFoundException("존재하지 않는 글");

       }
       Board board =optionalBoard.get();
       Optional<User> optionalUser = userRepository.findById(userId);
       if (optionalUser.isEmpty()){
           throw new ResourceNotFoundException("존재하지 않는 유저");

       }
       User user = optionalUser.get();
       // 1. 이 유저와  보드로 동일한 좋아요 있나 확인
        Optional<Like> likeOptional = likeRepository.findByUserIdAndBoardId(userId, id);

        // 2. 있으면 삭제
        if (likeOptional.isEmpty()){
            Like like = new Like();
            like.setUser(user);
            like.setBoard(board);

            likeRepository.save(like);

        }else {        // 3. 없으면 추가
            Like like = likeOptional.get();
            likeRepository.deleteById(like.getId());

        }






    }






}