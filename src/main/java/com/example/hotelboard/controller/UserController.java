package com.example.hotelboard.controller;

import com.example.hotelboard.dto.UsersForm;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;  // 사용자 관련 서비스 주입

    @GetMapping("/membership/login")  // 로그인 페이지 요청 처리
    public String login() {
        return "membership/login";  // 로그인 페이지 뷰 반환
    }

    @PostMapping("/membership/signUp")  // 회원가입 요청 처리
    public String signUpForm(@ModelAttribute UsersForm form, RedirectAttributes redirectAttributes) {
        // 사용자 ID 중복 체크
        if (userService.existsByUserId(form.getUserId())) {
            redirectAttributes.addFlashAttribute("error", "이미 있는 아이디입니다. 다시 입력해주세요.");  // 오류 메시지 추가
            return "redirect:/membership/login";  // 로그인 페이지로 리다이렉트
        }
        userService.signUp(form);  // 회원가입 처리
        redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");  // 성공 메시지 추가
        return "redirect:/membership/login";  // 로그인 페이지로 리다이렉트
    }

    @PostMapping("/membership/signIn")  // 로그인 요청 처리
    public String signInForm(@RequestParam String userId, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        // 사용자 인증
        Users loginEntity = userService.signIn(userId, password);

        if (loginEntity != null) {
            session.setAttribute("user", loginEntity);  // 세션에 사용자 정보 저장
            redirectAttributes.addFlashAttribute("success", "로그인 완료");  // 성공 메시지 추가
            return "redirect:/home";  // 홈 페이지로 리다이렉트
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");  // 오류 메시지 추가
            return "redirect:/membership/login";  // 로그인 페이지로 리다이렉트
        }
    }

    @GetMapping("/membership/logout")  // 로그아웃 요청 처리
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/";  // 메인 페이지로 리다이렉트
    }

    @GetMapping("/membership/checkUserId")  // 사용자 ID 중복 체크 요청 처리
    public @ResponseBody boolean checkUserId(@RequestParam String userId) {
        return userService.existsByUserId(userId);  // 사용자 ID의 존재 여부 반환
    }
}