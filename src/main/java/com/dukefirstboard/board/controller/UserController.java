package com.dukefirstboard.board.controller;


import com.dukefirstboard.board.entity.User;
import com.dukefirstboard.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        // 중복 검사
        if (userService.checkUsernameDuplicate(user.getUsername())) {
            return "redirect:/user/register?error=username";
        }
        if (userService.checkEmailDuplicate(user.getEmail())) {
            return "redirect:/user/register?error=email";
        }

        userService.register(user);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String editForm(Model model) {
        // 현재 로그인한 사용자 정보를 가져와 모델에 추가하는 로직이 필요합니다
        // Spring Security를 통해 현재 인증된 사용자 정보를 가져옵니다
        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/user/profile?username=" + user.getUsername();
    }
}
