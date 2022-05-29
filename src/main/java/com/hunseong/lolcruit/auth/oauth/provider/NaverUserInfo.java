package com.hunseong.lolcruit.auth.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Created by Hunseong on 2022/05/28
 */
@RequiredArgsConstructor
public class NaverUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
