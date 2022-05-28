package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.web.dto.user.EditRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean hasEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            // TODO user 객체의 SNS 여부에 따른 연동 처리
            return true;
        } else {
            return false;
        }
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

        editRequestDto.encodePassword(passwordEncoder.encode(editRequestDto.getPassword()));

        user.update(editRequestDto);

//        세션 변경 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), editRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
