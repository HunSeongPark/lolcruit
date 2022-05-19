package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
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

    public Page<PostResponseDto> findAll(Pageable pageable, Position position) {

        // ALL
        if (position == null) {
            Page<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
            return posts.map(PostResponseDto::fromEntity);
        }

        Page<Post> posts = postRepository.findAllByPositionOrderByIdDesc(pageable, position);
        return posts.map(PostResponseDto::fromEntity);
    }

}
