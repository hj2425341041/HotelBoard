package com.example.hotelboard.repository;

import com.example.hotelboard.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
    // 특정 날짜에 대한 예약 조회
    List<Reservations> findByCheckInLessThanEqualAndCheckOutGreaterThanEqual(Date startDate, Date endDate);
}
