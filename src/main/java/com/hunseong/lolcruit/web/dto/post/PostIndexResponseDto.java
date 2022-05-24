package com.hunseong.lolcruit.web.dto.post;

import com.hunseong.lolcruit.domain.post.Position;
import com.hunseong.lolcruit.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hunseong on 2022/05/19
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostIndexResponseDto {

    private Long id;
    private String title;
    private String writer;
    private Position position;
    private int view;
    private String createdDate;

    public static PostIndexResponseDto fromEntity(Post post) {
        return new PostIndexResponseDto(
                post.getId(),
                post.getTitle(),
                post.getWriter(),
                post.getPosition(),
                post.getView(),
                calcCreateDate(post.getCreatedDate())
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
