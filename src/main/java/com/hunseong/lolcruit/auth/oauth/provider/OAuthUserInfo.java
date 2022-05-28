package com.hunseong.lolcruit.auth.oauth.provider;

/**
 * Created by Hunseong on 2022/05/28
 */
public interface OAuthUserInfo {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
