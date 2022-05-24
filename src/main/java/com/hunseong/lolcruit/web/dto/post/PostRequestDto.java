package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.user.User;
import lombok.*;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {

    private String title;

    private String content;

    private String writer;

    private Position position;

    public Post toEntity(User writeUser) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .writer(writeUser.getNickname())
                .position(this.position)
                .user(writeUser)
                .build();
    }
}
