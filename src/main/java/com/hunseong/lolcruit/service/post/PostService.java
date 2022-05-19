package com.hunseong.lolcruit.service.post;

import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.post.PostRepository;
import com.hunseong.lolcruit.web.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hunseong on 2022/05/19
 */
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    // 게시글 리스트
    List<PostResponseDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostResponseDto::fromEntity).toList();
    }

}
