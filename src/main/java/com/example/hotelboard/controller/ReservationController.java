package com.example.hotelboard.controller;

import com.example.hotelboard.dto.ReservationForm;
import com.example.hotelboard.entity.Reservations;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.ReservationRepository;
import com.example.hotelboard.repository.UserRepository;
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
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    // 예약하는 페이지 폼 보여주는 메서드
    @GetMapping("/reserveNew")
    public String reserveNewForm(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

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
        Users user = (Users) session.getAttribute("user");

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
            Reservations reservations = reservationForm.toEntity(userRepository);
            log.info("Created Reservations entity: {}", reservations);

            Reservations savedReservation = reservationRepository.save(reservations);
            log.info("Saved reservation: {}", savedReservation);
        } catch (Exception e) {
            log.error("Error processing reservation", e);
            throw e;
        }

        return "redirect:/reserveList";
    }


    @GetMapping("/reserveList")
    public String showReservation(Model model) {
        List<Map<String, String>> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 30; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date date = calendar.getTime();
            String formattedDate = sdf.format(date);

            Map<String, String> row = new HashMap<>();
            row.put("date", formattedDate);

            // 날짜 범위 설정
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(date);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            Date startDate = startCalendar.getTime();

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(date);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            Date endDate = endCalendar.getTime();

            // 예약 상태 조회
            List<Reservations> reservations = reservationRepository.findByCheckInLessThanEqualAndCheckOutGreaterThanEqual(startDate, endDate);
            log.info("Date: {} - Reservations found: {}", formattedDate, reservations);

            // 룸 상태 초기화
            row.put("room1Status", "예약 가능");
            row.put("room2Status", "예약 가능");
            row.put("room3Status", "예약 가능");

            // 예약 상태를 업데이트
            for (Reservations reservation : reservations) {
                Long roomId = reservation.getRoomId();
                if (roomId == 1) {
                    row.put("room1Status", "예약 불가");
                } else if (roomId == 2) {
                    row.put("room2Status", "예약 불가");
                } else if (roomId == 3) {
                    row.put("room3Status", "예약 불가");
                }
            }

            dateList.add(row);
        }

        model.addAttribute("dateList", dateList);
        log.info("Date list prepared: {}", dateList);

        return "reservation/reserveList";
    }

    //예약 디테일 보여주는 메서드
    @GetMapping("/reserveList/{reservationId}")
    public String showReservationDetail(@PathVariable Long reservationId, Model model) {
        log.info("Fetching reservation details for ID={}", reservationId);

        Reservations reservationsEntity = reservationRepository.findById(reservationId).orElse(null);

        if (reservationsEntity == null) {
            log.error("Reservation with id={} not found.", reservationId);
            return "redirect:/reserveList";
        }

        log.info("Found reservation: {}", reservationsEntity);
        model.addAttribute("reserved", reservationsEntity);

        return "reservation/reservedDetail";
    }




}
