package com.hunseong.lolcruit.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Hunseong on 2022/05/24
 */
@RequiredArgsConstructor
@Getter
public enum LoginErrorCode {
    INVALID_ID_PASSWORD(4001, "아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(4002,"존재하지 않는 아이디입니다."),
    INTERNAL_ERROR(5001, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    ELSE_ERROR(5002, "알 수 없는 이유로 오류가 발생했습니다.");

    private final int code;
    private final String message;
}
