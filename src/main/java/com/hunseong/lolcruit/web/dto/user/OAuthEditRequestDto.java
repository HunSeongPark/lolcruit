package com.hunseong.lolcruit.web.dto.user;

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
public class OAuthEditRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\d-_]{2,15}$", message = "닉네임은 대소문자, 한글, 숫자 포함 2~15자리 입니다.")
    private String nickname;
}
