package com.example.hotelboard.controller;

import com.example.hotelboard.dto.UsersForm;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/membership/login")
    public String login() {
        return "membership/login";
    }

    @PostMapping("/membership/signUp")
    public String signUpForm(@ModelAttribute UsersForm form, RedirectAttributes redirectAttributes) {
        if (userRepository.existsByUserId(form.getUserId())) {
            redirectAttributes.addFlashAttribute("error", "이미 있는 아이디입니다. 다시 입력해주세요.");
            return "redirect:/membership/login";
        }
        Users users = form.toEntity();
        userRepository.save(users);
        redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
        return "redirect:/membership/login";
    }

    @PostMapping("/membership/signIn")
    public String signInForm(@RequestParam String userId, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        Users loginEntity = userRepository.findByUserIdAndPassword(userId, password);

        if (loginEntity != null) {
            session.setAttribute("user", loginEntity);
            redirectAttributes.addFlashAttribute("success", "로그인 완료");
            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            return "redirect:/membership/login";
        }
    }

    @GetMapping("/membership/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/membership/checkUserId")
    public @ResponseBody boolean checkUserId(@RequestParam String userId) {
        return userRepository.existsByUserId(userId);
    }
}
