package com.hunseong.lolcruit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
