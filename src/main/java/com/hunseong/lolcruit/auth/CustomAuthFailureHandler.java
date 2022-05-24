package com.hunseong.lolcruit.auth;

import com.hunseong.lolcruit.constants.LoginErrorMessageConst;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.hunseong.lolcruit.constants.LoginErrorMessageConst.*;

/**
 * Created by Hunseong on 2022/05/24
 */
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String redirectUrl = "/auth/login?error=true";
        String msg;

        // 비밀번호 불일치
        if (exception instanceof BadCredentialsException) {
            msg = INVALID_PASSWORD;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            msg = INTERNAL_ERROR;
        } else if (exception instanceof UsernameNotFoundException) {
            msg = USER_NOT_FOUND;
        } else {
            msg = ELSE_ERROR;
        }

        request.setAttribute("errorMessage", msg);
        request.getRequestDispatcher(redirectUrl).forward(request, response);
    }
}
