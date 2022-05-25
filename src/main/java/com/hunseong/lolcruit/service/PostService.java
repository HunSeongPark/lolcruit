package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.web.dto.comment.CommentResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostEditDto;
import com.hunseong.lolcruit.web.dto.post.PostRequestDto;
import com.hunseong.lolcruit.web.dto.post.PostIndexResponseDto;
import com.hunseong.lolcruit.web.dto.post.PostReadDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
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

    public Long add(PostRequestDto postRequestDto, SessionUser user) {
        User writeUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = postRequestDto.toEntity(writeUser);
        return postRepository.save(post).getId();
    }

    public PostReadDto findByIdForRead(Long id) {
        Post post = postRepository.findByIdFetchComments(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<CommentResponseDto> comments =
                post.getComments().stream().map(CommentResponseDto::fromEntity).toList();

        post.upView();

        return PostReadDto.fromEntity(post, comments);
    }

    public PostEditDto findByIdForEdit(Long id, SessionUser user) {
        Post post = postRepository.findByIdFetchUser(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getNickname().equals(user.getNickname())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return PostEditDto.fromEntity(post);
    }

    public void update(PostEditDto postEditDto, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.update(postEditDto);

    }

    public Long delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
        return post.getId();
    }
}
