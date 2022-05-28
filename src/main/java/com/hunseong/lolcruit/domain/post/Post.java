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

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content, String writer, Position position, User user) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.position = position;
        this.user = user;
    }

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
