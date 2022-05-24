package com.hunseong.lolcruit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Created by Hunseong on 2022/05/24
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleCustomException(CustomException e, Model model) {
        log.error("[Occur CustomException] - {}", e.getErrorCode().getDetail());

        model.addAttribute("code", e.getErrorCode().getHttpStatus().value());
        model.addAttribute("message", e.getErrorCode().getDetail());
        return "error/error";
    }

    @ExceptionHandler
    public String handleException(Exception e, Model model) {
        log.error("[Occur Internal Exception] - {}", e.getMessage(), e);
        model.addAttribute("code", INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", "서버 내부 오류입니다. 관리자에게 문의하세요.");
        return "error/error";
    }
}
