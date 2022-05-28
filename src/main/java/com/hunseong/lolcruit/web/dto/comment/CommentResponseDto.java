package com.hunseong.lolcruit.web.dto.comment;

import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hunseong on 2022/05/25
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private Long writerId;
    private String createdDate;

    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getUser().getId(),
                calcCreateDate(comment.getCreatedDate())
        );
    }

    private static String calcCreateDate(LocalDateTime createdDate) {
        LocalDate date = createdDate.toLocalDate();

        // 만약 댓글 작성일이 오늘 날짜라면 시간포맷만 return
        if (date.isEqual(LocalDate.now())) {
            return createdDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return createdDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
    }
}
