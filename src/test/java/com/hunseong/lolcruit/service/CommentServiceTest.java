package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.comment.CommentRepository;
import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.Role;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.web.dto.comment.CommentRequestDto;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Hunseong on 2022/05/26
 */
@Slf4j
@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 등록에 성공한다")
    @Test
    void add_success() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("user", "12", "user", "user@user.com");
        userService.join(joinRequestDto);

        SessionUser sessionUser = new SessionUser("user", "12", "user", "user@user.com", Role.USER);

        PostRequestDto postRequestDto = new PostRequestDto("title", "cont", "user", Position.TOP);
        Long postId = postService.add(postRequestDto, sessionUser);

        CommentRequestDto commentRequestDto = new CommentRequestDto("hi");

        // when
        Long commentId = commentService.add(sessionUser, postId, commentRequestDto);

        // then
        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.getContent()).isEqualTo(commentRequestDto.getContent());
        assertThat(comment.getUser().getUsername()).isEqualTo(sessionUser.getUsername());
        assertThat(comment.getPost().getId()).isEqualTo(postId);
    }

    @DisplayName("[찾을 수 없는 사용자]댓글 등록에 실패한다")
    @Test
    void add_fail_user_not_found() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("user", "12", "user", "user@user.com");
        userService.join(joinRequestDto);

        SessionUser sessionUser = new SessionUser("user", "12", "user", "user@user.com", Role.USER);
        SessionUser newUser = new SessionUser("user1", "12", "user1", "user1@user.com", Role.USER);

        PostRequestDto postRequestDto = new PostRequestDto("title", "cont", "user", Position.TOP);
        Long postId = postService.add(postRequestDto, sessionUser);

        CommentRequestDto commentRequestDto = new CommentRequestDto("hi");

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> commentService.add(newUser, postId, commentRequestDto));
        log.info(customException.getMessage());
    }

    @DisplayName("[찾을 수 없는 게시글]댓글 등록에 실패한다")
    @Test
    void add_fail_post_not_found() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("user", "12", "user", "user@user.com");
        userService.join(joinRequestDto);

        SessionUser sessionUser = new SessionUser("user", "12", "user", "user@user.com", Role.USER);

        PostRequestDto postRequestDto = new PostRequestDto("title", "cont", "user", Position.TOP);
        Long postId = postService.add(postRequestDto, sessionUser);

        CommentRequestDto commentRequestDto = new CommentRequestDto("hi");

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> commentService.add(sessionUser, postId + 1, commentRequestDto));
        log.info(customException.getMessage());
    }
}