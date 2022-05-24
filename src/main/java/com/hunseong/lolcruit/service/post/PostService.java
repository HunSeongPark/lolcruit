package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<PostResponseDto> findAll(Pageable pageable, Position position, String keyword) {

        // ALL
        if (position == null && (keyword == null || keyword.isBlank())) {
            Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
            return posts.map(PostResponseDto::fromEntity);
        }

        // ALL && Search
        if (position == null && !keyword.isBlank()) {
            Page<Post> posts = postRepository.findAllByTitleContainingOrderByIdDesc(pageable, keyword);
            return posts.map(PostResponseDto::fromEntity);
        }

        // position
        if (position != null && (keyword == null || keyword.isBlank())) {
            Page<Post> posts = postRepository.findAllByPositionOrderByIdDesc(pageable, position);
            return posts.map(PostResponseDto::fromEntity);
        }

        // position && Search
        Page<Post> posts = postRepository.findAllByPositionAndKeyword(pageable, position, keyword);
        return posts.map(PostResponseDto::fromEntity);


    }

    @Transactional
    public Long add(PostRequestDto postRequestDto, SessionUser user) {
        // TODO
        User writeUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        Post post = postRequestDto.toEntity(writeUser);
        return postRepository.save(post).getId();
    }
}
