package com.dukefirstboard.board.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", e.getClass().getSimpleName());
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 500);

        // 에러 페이지 반환
        return "error/error";
    }

    // 특정 예외 처리 (예: 리소스를 찾을 수 없는 경우)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model, HttpServletRequest request) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 404);

        // 에러 페이지 반환
        return "error/error";
    }

    // 더 많은 특정 예외 처리기를 추가할 수 있습니다
}