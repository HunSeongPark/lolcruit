package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.constants.EmailValidationResult;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.web.dto.user.EditRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.OAuthEditRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hunseong.lolcruit.constants.EmailValidationResult.*;
import static com.hunseong.lolcruit.constants.OAuthConst.PASSWORD_SECRET;

/**
 * Created by Hunseong on 2022/05/23
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public boolean hasUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean hasNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public int hasEmail(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if (user.getProvider() != null) {
                // SNS 아이디로 가입되어 있을 시
//                bindingResult.reject("snsExist", "이미 SNS 계정으로 가입된 이메일입니다.");
                return IS_EXIST_SNS;
            } else {
                // 일반 아이디로 가입되어 있을 시
//                bindingResult.reject("duplicateEmail", "이미 존재하는 이메일입니다.");
                return IS_EXIST_EMAIL;
            }
        }

        // 가입되어 있지 않을 시
        return OK;
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        joinRequestDto.encodePassword(passwordEncoder.encode(joinRequestDto.getPassword()));
        return userRepository.save(joinRequestDto.toEntity()).getId();
    }

    @Transactional
    public void edit(EditRequestDto editRequestDto, SessionUser sessionUser) {
        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodePw = passwordEncoder.encode(editRequestDto.getPassword());
        user.update(editRequestDto.getNickname(), encodePw);

        // Security 세션 변경 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), editRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void oauthEdit(OAuthEditRequestDto oAuthEditRequestDto, SessionUser sessionUser) {

        User user = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.update(oAuthEditRequestDto.getNickname(), user.getPassword());

        // Security 세션 변경 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), PASSWORD_SECRET)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
