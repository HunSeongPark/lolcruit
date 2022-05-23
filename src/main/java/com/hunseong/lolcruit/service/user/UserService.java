package com.hunseong.lolcruit.service.user;

import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.web.dto.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
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
    public Long join(UserRequestDto userRequestDto) {
        userRequestDto.encodePassword(passwordEncoder.encode(userRequestDto.getPassword()));
        return userRepository.save(userRequestDto.toEntity()).getId();
    }
}
