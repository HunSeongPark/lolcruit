package com.hunseong.lolcruit.domain.user;

import com.hunseong.lolcruit.domain.BaseTimeEntity;
import com.hunseong.lolcruit.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunseong on 2022/05/18
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();
}
