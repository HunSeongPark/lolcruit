package com.hunseong.lolcruit.domain.post;

import com.hunseong.lolcruit.domain.BaseTimeEntity;
import com.hunseong.lolcruit.domain.comment.Comment;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.web.dto.post.PostEditDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunseong on 2022/05/18
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String writer;

    @Enumerated(EnumType.STRING)
    private Position position;

    private int view;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Update Logic
    public void update(PostEditDto postEditDto) {
        this.title = postEditDto.getTitle();
        this.content = postEditDto.getContent();
        this.position = postEditDto.getPosition();
    }

    public void upView() {
        this.view ++;
    }
}
