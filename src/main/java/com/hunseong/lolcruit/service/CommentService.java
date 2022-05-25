package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.comment.CommentRepository;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.web.dto.comment.CommentDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hunseong.lolcruit.exception.ErrorCode.POST_NOT_FOUND;
import static com.hunseong.lolcruit.exception.ErrorCode.USER_NOT_FOUND;

/**
 * Created by Hunseong on 2022/05/25
 */
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long add(SessionUser sessionUser, Long postId, CommentDto commentDto) {
        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return commentRepository.save(commentDto.toEntity(user, post)).getId();
    }
}
