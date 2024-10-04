package com.example.hotelboard.service;

import com.example.hotelboard.dto.ReservationForm;
import com.example.hotelboard.entity.Reservations;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.ReservationRepository;
import com.example.hotelboard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void submitReservation(ReservationForm reservationForm, Users user) {
        Reservations reservations = reservationForm.toEntity(userRepository);
        log.info("Created Reservations entity: {}", reservations);
        reservationRepository.save(reservations);
    }

    @Override
    public List<Map<String, String>> getReservationStatus() {
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

        return dateList;
    }

    @Override
    public Reservations getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }
}
