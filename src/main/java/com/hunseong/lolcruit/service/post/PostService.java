package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.post.PostIndexResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<PostIndexResponseDto> findAll(Pageable pageable, Position position, String keyword) {

        // ALL
        if (position == null && (keyword == null || keyword.isBlank())) {
            Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
            return posts.map(PostIndexResponseDto::fromEntity);
        }

        // ALL && Search
        if (position == null && !keyword.isBlank()) {
            Page<Post> posts = postRepository.findAllByTitleContainingOrderByIdDesc(pageable, keyword);
            return posts.map(PostIndexResponseDto::fromEntity);
        }

        // position
        if (position != null && (keyword == null || keyword.isBlank())) {
            Page<Post> posts = postRepository.findAllByPositionOrderByIdDesc(pageable, position);
            return posts.map(PostIndexResponseDto::fromEntity);
        }

        // position && Search
        Page<Post> posts = postRepository.findAllByPositionAndKeyword(pageable, position, keyword);
        return posts.map(PostIndexResponseDto::fromEntity);


    }

    @Transactional
    public Long add(PostRequestDto postRequestDto, SessionUser user) {
        // TODO
        User writeUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        Post post = postRequestDto.toEntity(writeUser);
        return postRepository.save(post).getId();
    }

    public PostResponseDto findById(Long id) {
        // TODO
        Post post = postRepository.findByIdFetchComments(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        return PostResponseDto.fromEntity(post);
    }
}
