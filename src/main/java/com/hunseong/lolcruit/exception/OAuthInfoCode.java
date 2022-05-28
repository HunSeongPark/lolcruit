package com.hunseong.lolcruit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Hunseong on 2022/05/28
 */
@Getter
@RequiredArgsConstructor
public enum OAuthInfoCode {

    /* 일반 계정 -> SNS 연동 */
    SNS_INTEGRATED("해당 이메일로 가입된 일반 계정이 존재하여 SNS 계정이 연동되었습니다. " +
            "이후 해당 SNS 계정으로 로그인 해주세요."),

    /* 다른 SNS 계정 존재 */
    EXIST_ANOTHER_SNS("해당 이메일로 가입된 다른 SNS 계정이 존재합니다. 해당 SNS 계정으로 로그인 해주세요.");

    private final String detail;
}
