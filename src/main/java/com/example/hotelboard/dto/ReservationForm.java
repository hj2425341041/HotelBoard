package com.example.hotelboard.dto;

import com.example.hotelboard.entity.Reservations;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Slf4j
public class ReservationForm {
    private Long reservationId;
    private Long memberId;
    private Date checkIn;
    private Date checkOut;
    private Long roomId;
    private String inputName;
    private String memo;
    private Date reservedAt;
    private Reservations.Process reserveProcess;

    //DTO 개체를 Reservations 엔티티로 변환하는 메서드
    public Reservations toEntity() {
        return new Reservations(reservationId, new Users(memberId), checkIn, checkOut, roomId, inputName, memo, new Date(), reserveProcess);
    }

    public Reservations toEntity(UserRepository userRepository) {
        // `memberId`를 사용하여 `Users` 객체를 조회
        Users user = userRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        return new Reservations(
                user,        // 조회된 `Users` 객체
                checkIn,
                checkOut,
                roomId,
                inputName,
                memo,
                new Date(),
                reserveProcess
        );
    }





}
