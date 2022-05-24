package com.hunseong.lolcruit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Hunseong on 2022/05/24
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
