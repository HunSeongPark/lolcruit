package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.Role;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.service.PostService;
import com.hunseong.lolcruit.service.UserService;
import com.hunseong.lolcruit.web.dto.post.PostIndexResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostReadDto;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

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

    @Test
    void add_success() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        userService.join(joinRequestDto);


        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        // when
        Long postId = postService.add(postRequestDto, sessionUser);

        // then
        PostReadDto result = postService.findByIdForRead(postId);
        assertThat(result.getContent()).isEqualTo(postRequestDto.getContent());
        assertThat(result.getWriter()).isEqualTo(postRequestDto.getWriter());
        assertThat(result.getPosition()).isEqualTo(postRequestDto.getPosition());
    }

    @Test
    void add_fail_not_found_user() {

        // given
        PostRequestDto postRequestDto = new PostRequestDto("title", "content", "hunseong", Position.MID);
        SessionUser sessionUser = new SessionUser("hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER);

        // when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.add(postRequestDto, sessionUser));
        log.info(customException.getMessage());
    }

    @Test
    void findByIdForRead_fail_post_not_found() {

        // given & when & then
        CustomException customException = assertThrows(CustomException.class,
                () -> postService.findByIdForRead(1L));
        log.info(customException.getMessage());

    }
}