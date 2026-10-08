package com.green.spring_board.service;

import com.green.spring_board.dto.CommentCreateRequest;
import com.green.spring_board.dto.CommentResponse;
import com.green.spring_board.dto.UpdateCommentRequest;
import com.green.spring_board.entity.Board;
import com.green.spring_board.entity.Comment;
import com.green.spring_board.entity.User;
import com.green.spring_board.exceptions.AuthorizationFailureException;
import com.green.spring_board.exceptions.ResourceNotFoundException;
import com.green.spring_board.repository.BoardRepository;
import com.green.spring_board.repository.CommentRepository;
import com.green.spring_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service  // 서비스 클래스로 등록
@RequiredArgsConstructor // final 필드 생성자 자동 생성
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void createComment(CommentCreateRequest commentCreateRequest,
                              int userId, int boardId ){
        Optional<User> optionalUser   =userRepository.findById(userId);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        // 비었으면
        if(optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
        }
        if(optionalBoard.isEmpty()) {
            throw new ResourceNotFoundException("글을 찾을수 없음");
        }
      // 글이 있으면 꺼내온다
        Board board = optionalBoard.get();
        User user = optionalUser.get();

        Comment comment = new Comment();
        comment.setContent(commentCreateRequest.getContent());
        comment.setUser(user);
        comment.setBoard(board);

        commentRepository.save(comment);


    }
    public List<CommentResponse> readComments(int boardId) {
      if (!boardRepository.existsById(boardId)){
          throw new ResourceNotFoundException("board not found");

      }
        List<Comment> comments = commentRepository.findByBoardId(boardId);

        List<CommentResponse> commentResponses = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(comment.getId());
            commentResponse.setContent(comment.getContent());
            commentResponse.setNickname(comment.getUser().getNickname());
            commentResponse.setCommentDate(comment.getCreatedDatetime());

            commentResponses.add(commentResponse);
        }

        return commentResponses;
    }

    public void updateComment(int id, UpdateCommentRequest updateCommentRequest, int userId){
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()){
            throw new ResourceNotFoundException(" 댓글을 찾을 수 없습니다");

        }
        // 댓글이 있으면 꺼낸다
        Comment comment=optionalComment.get();
// 작성자 아니면 수정 막음
        if (comment.getUser().getId() != userId){
            throw new AuthorizationFailureException("권한이 없습니다");

        }

        // 수정
        comment.setContent(updateCommentRequest.getContent());
        commentRepository.save(comment);

    }

    public void deleteComment(int id, int userId){
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()){
            throw new ResourceNotFoundException(" 댓글을 찾을 수 없습니다");

        }
        // 댓글이 있으면 꺼낸다
        Comment comment=optionalComment.get();

        if (comment.getUser().getId() != userId){
            throw new AuthorizationFailureException("권한이 없습니다");

        }
        commentRepository.deleteById(id);


    }


}

