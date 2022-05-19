package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;

    // 게시글 리스트
    public Page<PostResponseDto> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        return posts.map(PostResponseDto::fromEntity);
    }

}
