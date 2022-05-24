package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {

    private String title;
    private String content;
    private String writer;
    private Position position;
    private String createdAt;
    private List<Comment> comments;

    public static PostResponseDto fromEntity(Post post) {
        return new PostResponseDto(
                post.getTitle(),
                post.getContent(),
                post.getWriter(),
                post.getPosition(),
                calcCreateDate(post.getCreatedDate()),
                post.getComments()
        );
    }

    private static String calcCreateDate(LocalDateTime createdDate) {
        LocalDate date = createdDate.toLocalDate();

        // 만약 게시글이 오늘 날짜라면 시간포맷만 return
        if (date.isEqual(LocalDate.now())) {
            return createdDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return createdDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
    }
}
