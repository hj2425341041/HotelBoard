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
    private ReservationRepository reservationRepository;  // 예약 관련 데이터 접근 레포지토리 주입

    @Autowired
    private UserRepository userRepository;  // 사용자 관련 데이터 접근 레포지토리 주입

    @Override
    public void submitReservation(ReservationForm reservationForm, Users user) {
        // 예약을 제출하고 저장
        Reservations reservations = reservationForm.toEntity(userRepository);  // ReservationForm을 Reservations 엔티티로 변환
        log.info("Created Reservations entity: {}", reservations);  // 생성된 예약 정보 로그 출력
        reservationRepository.save(reservations);  // 예약 데이터베이스에 저장
    }

    @Override
    public List<Map<String, String>> getReservationStatus() {
        // 예약 상태를 조회하여 30일간의 룸 상태 정보를 반환
        List<Map<String, String>> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();  // 현재 날짜 기준으로 캘린더 인스턴스 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // 날짜 포맷 설정

        // 30일 동안 반복
        for (int i = 0; i < 30; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);  // 하루씩 추가
            Date date = calendar.getTime();  // 현재 날짜 가져오기
            String formattedDate = sdf.format(date);  // 날짜 포맷팅
            Map<String, String> row = new HashMap<>();
            row.put("date", formattedDate);  // 날짜 추가

            // 날짜 범위 설정 (시작일과 종료일)
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(date);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            Date startDate = startCalendar.getTime();  // 시작일

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(date);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            Date endDate = endCalendar.getTime();  // 종료일

            // 예약 상태 조회
            List<Reservations> reservations = reservationRepository.findByCheckInLessThanEqualAndCheckOutGreaterThanEqual(startDate, endDate);
            log.info("Date: {} - Reservations found: {}", formattedDate, reservations);  // 예약 정보 로그 출력

            // 룸 상태 초기화 (모든 룸은 예약 가능으로 설정)
            row.put("room1Status", "예약 가능");
            row.put("room2Status", "예약 가능");
            row.put("room3Status", "예약 가능");

            // 예약 상태를 업데이트 (예약된 룸을 예약 불가로 설정)
            for (Reservations reservation : reservations) {
                Long roomId = reservation.getRoomId();  // 예약된 룸 ID 조회
                if (roomId == 1) {
                    row.put("room1Status", "예약 불가");  // 룸 1 예약 불가
                } else if (roomId == 2) {
                    row.put("room2Status", "예약 불가");  // 룸 2 예약 불가
                } else if (roomId == 3) {
                    row.put("room3Status", "예약 불가");  // 룸 3 예약 불가
                }
            }

            dateList.add(row);  // 날짜와 룸 상태 정보를 리스트에 추가
        }

        return dateList;  // 예약 상태 정보 반환
    }

    @Override
    public Reservations getReservationById(Long reservationId) {
        // ID로 예약 정보 조회
        return reservationRepository.findById(reservationId).orElse(null);  // 예약이 없으면 null 반환
    }
}