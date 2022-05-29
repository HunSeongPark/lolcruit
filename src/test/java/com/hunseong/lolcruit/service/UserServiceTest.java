package com.hunseong.lolcruit.service;

import com.hunseong.lolcruit.constants.EmailValidationResult;
import com.hunseong.lolcruit.domain.user.Role;
import com.hunseong.lolcruit.domain.user.User;
import com.hunseong.lolcruit.domain.user.UserRepository;
import com.hunseong.lolcruit.web.dto.user.EditRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Hunseong on 2022/05/26
 */
@Slf4j
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원가입에 성공한다")
    @Test
    void join() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");

        // when
        userService.join(joinRequestDto);

        // then
        boolean hasNickname = userService.hasNickname(joinRequestDto.getNickname());
        int hasEmail = userService.hasEmail(joinRequestDto.getEmail());
        boolean hasUsername = userService.hasUsername(joinRequestDto.getUsername());
        User user = userRepository.findByUsername(joinRequestDto.getUsername())
                        .get();

        assertThat(hasNickname).isTrue();
        assertThat(hasEmail).isEqualTo(EmailValidationResult.IS_EXIST_EMAIL);
        assertThat(hasUsername).isTrue();
        assertThat(user.getUsername()).isEqualTo(joinRequestDto.getUsername());
        assertThat(user.getNickname()).isEqualTo(joinRequestDto.getNickname());
    }

    @DisplayName("회원정보 수정에 성공한다")
    @Test
    void edit() {

        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("hunseong", "1234", "hunseong", "gnstjd@naver.com");
        Long savedUserId = userService.join(joinRequestDto);

        SessionUser user = new SessionUser(savedUserId, "hunseong", "1234", "hunseong", "gnstjd@naver.com", Role.USER, null);

        EditRequestDto editRequestDto = new EditRequestDto("1234", "new");

        // when
        userService.edit(editRequestDto, user);

        // then
        User findUser = userRepository.findById(savedUserId).get();
        assertThat(findUser.getNickname()).isEqualTo(editRequestDto.getNickname());
    }
}