package com.hunseong.lolcruit.web.dto.user;

import com.hunseong.lolcruit.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by Hunseong on 2022/05/23
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-z\\d]{6,15}$", message = "아이디는 소문자, 숫자 포함 6~15자리 입니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,15}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자 포함 8~15자리 입니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\d-_]{2,15}$", message = "닉네임은 대소문자, 한글, 숫자 포함 2~15자리 입니다.")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .email(this.email)
                .build();
    }

    public void encodePassword(String password) {
        this.password = password;
    }
}
