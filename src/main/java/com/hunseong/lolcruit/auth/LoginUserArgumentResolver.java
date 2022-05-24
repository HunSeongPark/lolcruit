package com.hunseong.lolcruit.auth;

import com.hunseong.lolcruit.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

/**
 * Created by Hunseong on 2022/05/24
 */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        // @LoginUser를 가지고 있는지
        boolean hasAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;

        // SessionUser Class인지
        boolean isSessionUser = SessionUser.class.equals(parameter.getParameterType());

        return hasAnnotation && isSessionUser;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
