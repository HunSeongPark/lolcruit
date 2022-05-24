package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.user.User;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 30, message = "제목은 최대 30자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
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
