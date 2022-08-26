package com.hunseong.lolcruit.web;

import com.hunseong.lolcruit.auth.LoginErrorCode;
import com.hunseong.lolcruit.auth.LoginUser;
import com.hunseong.lolcruit.exception.CustomException;
import com.hunseong.lolcruit.exception.ErrorCode;
import com.hunseong.lolcruit.service.UserService;
import com.hunseong.lolcruit.web.dto.user.EditRequestDto;
import com.hunseong.lolcruit.web.dto.user.JoinRequestDto;
import com.hunseong.lolcruit.web.dto.user.OAuthEditRequestDto;
import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.hunseong.lolcruit.constants.EmailValidationResult.*;

/**
 * Created by Hunseong on 2022/05/23
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("user") JoinRequestDto user) {
        return "auth/joinForm";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("user") JoinRequestDto joinRequestDto,
                       BindingResult bindingResult) {

        // 회원가입 실패 (validation error)
        if (bindingResult.hasErrors()) {
            return "auth/joinForm";
        }

        boolean isError = false;
        // 중복 아이디 (global error)
        if (userService.hasUsername(joinRequestDto.getUsername())) {
            bindingResult.reject("duplicateId", "이미 존재하는 아이디입니다.");
            isError = true;
        }

        // 중복 닉네임 (global error)
        if (userService.hasNickname(joinRequestDto.getNickname())) {
            bindingResult.reject("duplicateNickname", "이미 존재하는 닉네임입니다.");
            isError = true;
        }

        int hasEmail = userService.hasEmail(joinRequestDto.getEmail());

        // 중복 이메일 - SNS 계정 가입 (global error)
        if (hasEmail == IS_EXIST_SNS) {
            bindingResult.reject("snsExist", "이미 SNS 계정으로 가입된 이메일입니다.");
            isError = true;
        }

        // 중복 이메일 - 일반 계정 가입 (global error)
        if (hasEmail == IS_EXIST_EMAIL) {
            bindingResult.reject("duplicateEmail", "이미 존재하는 이메일입니다.");
            isError = true;
        }

        if (isError) {
            return "auth/joinForm";
        }

        userService.join(joinRequestDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm(
            @RequestParam(value = "error", required = false) Boolean isError,
            @RequestParam(value = "code", required = false) Integer code,
            HttpServletRequest request,
            Model model) {

        if (isError != null) {
            LoginErrorCode[] errors = LoginErrorCode.values();

            LoginErrorCode loginErrorCode = Arrays.stream(errors)
                    .filter(e -> e.getCode() == code)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            model.addAttribute("isError", isError);
            model.addAttribute("errorMessage", loginErrorCode.getMessage());
        }

        /**
         * 이전 페이지로 되돌아가기 위한 Referer 헤더값을 세션의 prevPage attribute로 저장
         * uri.contains를 통해 중복 저장되지 않도록 처리
         */
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        return "auth/loginForm";
    }

    @GetMapping("/edit")
    public String editForm(
            @LoginUser SessionUser user,
            @ModelAttribute("user") EditRequestDto editRequestDto,
            Model model
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        model.addAttribute("sessionUser", user);
        return "auth/editForm";
    }

    @PostMapping("/edit")
    public String edit(
            @LoginUser SessionUser user,
            @Validated @ModelAttribute("user") EditRequestDto editRequestDto,
            BindingResult bindingResult,
            Model model
    ) {

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        model.addAttribute("sessionUser", user);
        // 회원정보 수정 실패 (validation error)
        if (bindingResult.hasErrors()) {
            return "auth/editForm";
        }

        // 중복 닉네임 (global error)
        if (userService.hasNickname(editRequestDto.getNickname())) {
            bindingResult.reject("duplicateNickname", "이미 존재하는 닉네임입니다.");
            return "auth/editForm";
        }

        userService.edit(editRequestDto, user);

        return "redirect:/";
    }

    @GetMapping("/edit/oauth")
    public String oauthEditForm(
            @LoginUser SessionUser user,
            @ModelAttribute("user") OAuthEditRequestDto oAuthEditRequestDto,
            Model model
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        model.addAttribute("sessionUser", user);
        return "auth/oauthEditForm";
    }

    @PostMapping("/edit/oauth")
    public String oauthEdit(
            @LoginUser SessionUser user,
            @Validated @ModelAttribute("user") OAuthEditRequestDto oAuthEditRequestDto,
            BindingResult bindingResult,
            Model model
    ) {

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        model.addAttribute("sessionUser", user);

        // 회원정보 수정 실패 (validation error)
        if (bindingResult.hasErrors()) {
            return "auth/oauthEditForm";
        }

        // 중복 닉네임 (global error)
        if (userService.hasNickname(oAuthEditRequestDto.getNickname())) {
            bindingResult.reject("duplicateNickname", "이미 존재하는 닉네임입니다.");
            return "auth/editForm";
        }

        userService.oauthEdit(oAuthEditRequestDto, user);

        return "redirect:/";
    }
}
