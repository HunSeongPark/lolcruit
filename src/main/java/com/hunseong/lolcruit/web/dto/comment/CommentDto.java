package com.hunseong.lolcruit.web.dto.comment;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

/**
 * Created by Hunseong on 2022/05/25
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {

    private String content;

    public Comment toEntity(User user, Post post) {
        return new Comment(this.content, user, post);
    }
}
