package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.constants.LoginErrorCode;
import com.hunseong.lolcruit.service.user.UserService;
import com.hunseong.lolcruit.web.dto.user.LoginRequestDto;
import com.hunseong.lolcruit.web.dto.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Hunseong on 2022/05/23
 */
@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("user") UserRequestDto user) {
        return "auth/joinForm";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("user") UserRequestDto userRequestDto,
                       BindingResult bindingResult) {

        // 회원가입 실패 (validation error)
        if (bindingResult.hasErrors()) {
            return "auth/joinForm";
        }

        boolean isError = false;
        // 중복 아이디 (global error)
        if (userService.hasUsername(userRequestDto.getUsername())) {
            bindingResult.reject("duplicateId", "이미 존재하는 아이디입니다.");
            isError = true;
        }

        // 중복 닉네임 (global error)
        if (userService.hasNickname(userRequestDto.getNickname())) {
            bindingResult.reject("duplicateNickname", "이미 존재하는 닉네임입니다.");
            isError = true;
        }

        // 중복 이메일 (global error)
        if (userService.hasEmail(userRequestDto.getEmail())) {
            bindingResult.reject("duplicateEmail", "이미 존재하는 이메일입니다.");
            isError = true;
        }

        if (isError) {
            return "auth/joinForm";
        }

        userService.join(userRequestDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute("user") LoginRequestDto loginRequestDto,
            @RequestParam(value = "error", required = false) Boolean isError,
            @RequestParam(value = "code", required = false) Integer code,
            Model model) {

        if (isError != null) {
            LoginErrorCode[] errors = LoginErrorCode.values();

            // TODO : EXception handling
            LoginErrorCode loginErrorCode = Arrays.stream(errors)
                    .filter(e -> e.getCode() == code)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            model.addAttribute("isError", isError);
            model.addAttribute("errorMessage", loginErrorCode.getMessage());
        }
        return "auth/loginForm";
    }
}
