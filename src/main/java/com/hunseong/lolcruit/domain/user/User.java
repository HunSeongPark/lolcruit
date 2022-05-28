package com.hunseong.lolcruit.domain.user;

import com.hunseong.lolcruit.domain.BaseTimeEntity;
import com.hunseong.lolcruit.domain.post.Post;
import com.hunseong.lolcruit.web.dto.user.EditRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    private String provider;

    private String providerId;

    @Builder
    public User(String username, String password, String nickname,
                String email, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = Role.USER;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void update(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void oAuthIntegrate(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }
}
