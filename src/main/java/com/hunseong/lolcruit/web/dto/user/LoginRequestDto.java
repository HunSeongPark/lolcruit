package com.hunseong.lolcruit.web.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    private String username;
    private String password;
}
