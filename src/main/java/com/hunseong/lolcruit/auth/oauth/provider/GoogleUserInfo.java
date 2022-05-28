package com.hunseong.lolcruit.auth.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Created by Hunseong on 2022/05/28
 */
@RequiredArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
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
