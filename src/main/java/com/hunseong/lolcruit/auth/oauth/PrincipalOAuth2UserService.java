package com.hunseong.lolcruit.auth.oauth;

import com.hunseong.lolcruit.auth.PrincipalUserDetails;
import com.hunseong.lolcruit.auth.oauth.provider.GoogleUserInfo;
import com.hunseong.lolcruit.auth.oauth.provider.NaverUserInfo;
import com.hunseong.lolcruit.auth.oauth.provider.OAuthUserInfo;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.exception.OAuthInfoCode;
import com.hunseong.lolcruit.exception.OAuthInfoException;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hunseong.lolcruit.constants.OAuthConst.PASSWORD_SECRET;

/**
 * Created by Hunseong on 2022/05/28
 */
@RequiredArgsConstructor
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuthUserInfo userInfo;

        // 구글 로그인
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            // 네이버 로그인
            userInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
        } else {
            throw new CustomException(ErrorCode.USER_BAD_REQUEST);
        }

        String provider = userInfo.getProvider();
        String providerId = userInfo.getProviderId();
        String username = provider + "_" + providerId.substring(0, 5);
        String password = passwordEncoder.encode(PASSWORD_SECRET);
        String email = userInfo.getEmail();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        // 해당 email로 가입되어 있지 않을 때
        if (user == null) {
            user = User.builder()
                    .username(username)
                    .nickname(username)
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            User savedUser = userRepository.save(user);
            SessionUser sessionUser = SessionUser.fromEntity(savedUser);
            httpSession.setAttribute("user", sessionUser);
        } else {
            // 해당 email로 이미 가입되어 있을 때

            // 일반 계정이면 SNS 통합
            if (user.getProvider() == null) {
                user.oAuthIntegrate(provider, providerId, password);
                userRepository.save(user);
                httpSession.setAttribute("oauthInfo", OAuthInfoCode.SNS_INTEGRATED);
            } else {
                // 다른 SNS 계정일 때
                if (!user.getProvider().equals(provider)) {
                    httpSession.setAttribute("oauthInfo", OAuthInfoCode.EXIST_ANOTHER_SNS);
                } else {
                    // 해당 계정이 맞을 때
                    SessionUser sessionUser = SessionUser.fromEntity(user);
                    httpSession.setAttribute("user", sessionUser);
                }
            }
        }

        return new PrincipalUserDetails(user, oAuth2User.getAttributes());
    }
}
