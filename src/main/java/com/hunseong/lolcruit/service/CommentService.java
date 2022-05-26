package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.comment.CommentRepository;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.web.dto.comment.CommentRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hunseong.lolcruit.exception.ErrorCode.*;

/**
 * Created by Hunseong on 2022/05/25
 */
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long add(SessionUser sessionUser, Long postId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return commentRepository.save(commentRequestDto.toEntity(user, post)).getId();
    }

    public Long delete(SessionUser sessionUser, Long postId, Long commentId) {

        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Comment comment = commentRepository.findByIdFetchUser(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        validate(postId, user, comment);

        commentRepository.delete(comment);

        return comment.getId();
    }

    public Long edit(SessionUser sessionUser, Long postId,
                     Long commentId, CommentRequestDto commentRequestDto) {

        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        Comment comment = commentRepository.findByIdFetchUser(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        validate(postId, user, comment);

        comment.edit(commentRequestDto.getContent());

        return comment.getId();
    }

    private void validate(Long postId, User user, Comment comment) {

        boolean isPostExist = postRepository.existsById(postId);

        if (!isPostExist) {
            throw new CustomException(POST_NOT_FOUND);
        }

        if (!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new CustomException(UNAUTHORIZED_USER);
        }
    }
}
