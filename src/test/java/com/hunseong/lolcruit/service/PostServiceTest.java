package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.Role;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.service.PostService;
import com.hunseong.lolcruit.service.UserService;
import com.hunseong.lolcruit.web.dto.post.PostEditDto;
import com.hunseong.lolcruit.web.dto.post.PostIndexResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostReadDto;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Hunseong on 2022/05/19
 */
@Slf4j
@Transactional
@SpringBootTest
class PostServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @DisplayName("모든 게시글을 가져온다")
    @Test
    void findAll() {

        // given
        Post post1 = Post.builder()
                .title("title1")
                .writer("writer1")
                .position(Position.TOP)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .writer("writer2")
                .position(Position.MID)
                .build();

        Post savedPost1 = postRepository.save(post1);
        Post savedPost2 = postRepository.save(post2);

        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<PostIndexResponseDto> result = postService.findAll(pageable, null, null);

        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(savedPost2.getId());
        assertThat(result.getContent().get(1).getId()).isEqualTo(savedPost1.getId());
    }

    @DisplayName("포지션에 해당하는 게시글을 모두 가져온다.")
    @Test
    void findAllByPosition() {

        // given
        Post post1 = Post.builder()
                .title("title1")
                .writer("writer1")
                .position(Position.TOP)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .writer("writer2")
                .position(Position.MID)
                .build();

        Post post3 = Post.builder()
                .title("title3")
                .writer("writer3")
                .position(Position.TOP)
                .build();

        Post savedPost1 = postRepository.save(post1);
        Post savedPost2 = postRepository.save(post2);
        Post savedPost3 = postRepository.save(post3);

        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<PostIndexResponseDto> result = postService.findAll(pageable, Position.TOP, null);

        // then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(savedPost3.getId());
        assertThat(result.getContent().get(1).getId()).isEqualTo(savedPost1.getId());
    }

    @DisplayName("게시글 등록에 성공한다")
    @Test
    void add_success() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        Long savedUserId = userService.join(joinRequestDto);


        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser(savedUserId, "hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER, null);

        // when
        Long postId = postService.add(postRequestDto, sessionUser);

        // then
        PostReadDto result = postService.findByIdForRead(postId);
        assertThat(result.getContent()).isEqualTo(postRequestDto.getContent());
        assertThat(result.getWriter()).isEqualTo(postRequestDto.getWriter());
        assertThat(result.getPosition()).isEqualTo(postRequestDto.getPosition());
    }

    @DisplayName("[등록되지 않은 사용자] 게시글 등록에 실패한다")
    @Test
    void add_fail_not_found_user() {

        // given
        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser(3L, "hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER, null);

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.add(postRequestDto, sessionUser));
        log.info(customException.getMessage());
    }

    @DisplayName("[존재하지 않는 게시글] 게시글 읽기에 실패한다")
    @Test
    void findByIdForRead_fail_post_not_found() {

        // given & when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.findByIdForRead(1L));
        log.info(customException.getMessage());

    }

    @DisplayName("수정을 위한 게시글 가져오기에 성공한다")
    @Test
    void findByIdForEdit_success() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);


        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        SessionUser user = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        // when
        PostEditDto result = postService.findByIdForEdit(postId, user);

        // then
        assertThat(result.getId()).isEqualTo(postId);
        assertThat(result.getContent()).isEqualTo(postRequestDto.getContent());
    }

    @DisplayName("[인증되지 않은 사용자] 수정을 위한 게시글 가져오기에 실패한다")
    @Test
    void findByIdForEdit_fail_unauthorized_user() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);

        JoinRequestDto joinRequestDto2 = new JoinRequestDto("new", "1234", "new", "new@naver.com");
        userService.join(joinRequestDto2);


        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        SessionUser newUser = new SessionUser("new", "1234", "new", "new@naver.com", Role.USER);

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.findByIdForEdit(postId, newUser));
        log.info(customException.getMessage());
    }

    @DisplayName("게시글 수정에 성공한다")
    @Test
    void update() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);

        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        PostEditDto postEditDto = new PostEditDto(postId, "newTitle", "newContent", Position.TOP);

        // when
        postService.update(postEditDto, postId);
        em.flush();
        em.clear();

        // then
        PostReadDto result = postService.findByIdForRead(postId);
        assertThat(result.getTitle()).isEqualTo(postEditDto.getTitle());
        assertThat(result.getContent()).isEqualTo(postEditDto.getContent());
        assertThat(result.getPosition()).isEqualTo(postEditDto.getPosition());
    }

    @DisplayName("게시글 삭제에 성공한다")
    @Test
    void delete_success() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);

        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        // when
        Long deletedPostId = postService.delete(postId, sessionUser);

        // then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.findByIdForRead(deletedPostId));
        log.info(customException.getMessage());
    }

    @DisplayName("[찾을 수 없는 사용자]게시글 삭제에 실패한다")
    @Test
    void delete_fail_user_not_found() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);

        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        SessionUser newUser = new SessionUser("new", "1234", "new", "new@naver.com", Role.USER);

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.delete(postId, newUser));
        log.info(customException.getMessage());
    }

    @DisplayName("[인증되지 않은 사용자]게시글 삭제에 실패한다")
    @Test
    void delete_fail_unauthorized_user() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);

        JoinRequestDto joinRequestDto2 = new JoinRequestDto("new", "1234", "new", "new@naver.com");
        userService.join(joinRequestDto2);

        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        Long postId = postService.add(postRequestDto, sessionUser);

        SessionUser newUser = new SessionUser("new", "1234", "new", "new@naver.com", Role.USER);

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.delete(postId, newUser));
        log.info(customException.getMessage());
    }
}