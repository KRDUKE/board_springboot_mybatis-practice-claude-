package com.dukefirstboard.board.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        logger.error("예외 발생: ", e);
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
        logger.error("리소스를 찾을 수 없음: ", e);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 404);

        // 에러 페이지 반환
        return "error/error";
    }

    // 파일 업로드 오류 처리 추가
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException e, Model model, HttpServletRequest request) {
        logger.error("파일 처리 중 오류 발생: ", e);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", "File Processing Error");
        model.addAttribute("message", "파일을 처리하는 중 오류가 발생했습니다: " + e.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 500);

        return "error/error";
    }
}