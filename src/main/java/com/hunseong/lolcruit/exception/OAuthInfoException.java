package com.hunseong.lolcruit.exception;

import lombok.Getter;

/**
 * Created by Hunseong on 2022/05/28
 */
@Getter
public class OAuthInfoException extends RuntimeException {

    private final OAuthInfoCode oAuthInfoCode;

    public OAuthInfoException(OAuthInfoCode oAuthInfoCode) {
        super(oAuthInfoCode.getDetail());
        this.oAuthInfoCode = oAuthInfoCode;
    }
}
