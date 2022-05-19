package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunseong on 2022/05/19
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private Position position;
    private int view;

    public static PostResponseDto fromEntity(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter(),
                post.getPosition(),
                post.getView()
        );
    }
}
