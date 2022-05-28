package com.hunseong.lolcruit.web.dto.user;

import com.hunseong.lolcruit.domain.user.Role;
import com.hunseong.lolcruit.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor
public class SessionUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;
    private String provider;

    public static SessionUser fromEntity(User user) {
         return new SessionUser(
                 user.getId(),
                 user.getUsername(),
                 user.getPassword(),
                 user.getNickname(),
                 user.getEmail(),
                 user.getRole(),
                 user.getProvider()
         );
    }
}
