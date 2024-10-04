package com.example.hotelboard.controller;

import com.example.hotelboard.dto.ReservationForm;
import com.example.hotelboard.entity.Reservations;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.service.ReservationService;
import com.example.hotelboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    // 예약하는 페이지 폼 보여주는 메서드
    @GetMapping("/reserveNew")
    public String reserveNewForm(Model model, HttpSession session) {
        Users user = userService.getCurrentUser(session); // UserService를 통해 사용자 가져오기
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("phoneNum", user.getPhoneNum());
        } else {
            return "redirect:/membership/login";
        }
        return "/reservation/reserveNew";
    }

    // 날짜 형식 변환기 추가
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @PostMapping("/submit-reservation")
    public String submitReservation(@ModelAttribute ReservationForm reservationForm, HttpSession session, RedirectAttributes redirectAttributes) {
        Users user = userService.getCurrentUser(session); // UserService를 통해 사용자 가져오기
        log.info("Received reservation form: {}", reservationForm);
        log.info("Session user: {}", user);

        if (user == null) {
            log.warn("No user found in session, redirecting to login.");
            return "redirect:/membership/login";
        }

        // `memberId`를 세션에서 가져온 사용자 ID로 설정
        if (reservationForm.getMemberId() == null) {
            reservationForm.setMemberId(user.getMemberId());
        }

        if (reservationForm.getMemberId() == null || reservationForm.getRoomId() == null) {
            log.error("ReservationForm has null memberId or roomId: memberId={}, roomId={}", reservationForm.getMemberId(), reservationForm.getRoomId());
            return "redirect:/reservation/reserveNew";
        }

        try {
            reservationService.submitReservation(reservationForm, user);
        } catch (Exception e) {
            log.error("Error processing reservation", e);
            throw e;
        }

        return "redirect:/reserveList";
    }

    @GetMapping("/reserveList")
    public String showReservation(Model model) {
        List<Map<String, String>> dateList = reservationService.getReservationStatus();
        model.addAttribute("dateList", dateList);
        log.info("Date list prepared: {}", dateList);
        return "reservation/reserveList";
    }

    // 예약 디테일 보여주는 메서드
    @GetMapping("/reserveList/{reservationId}")
    public String showReservationDetail(@PathVariable Long reservationId, Model model) {
        Reservations reservationsEntity = reservationService.getReservationById(reservationId);
        if (reservationsEntity == null) {
            log.error("Reservation with id={} not found.", reservationId);
            return "redirect:/reserveList";
        }
        model.addAttribute("reserved", reservationsEntity);
        return "reservation/reservedDetail";
    }
}
