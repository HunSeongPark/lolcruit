package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor
public class PostEditDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 30, message = "제목은 최대 30자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Position position;

    public static PostEditDto fromEntity(Post post) {
        return new PostEditDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPosition()
        );
    }
}
