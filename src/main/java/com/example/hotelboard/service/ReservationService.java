package com.example.hotelboard.service;

import com.example.hotelboard.dto.ReservationForm;
import com.example.hotelboard.entity.Reservations;
import com.example.hotelboard.entity.Users;

import java.util.List;
import java.util.Map;

public interface ReservationService {
    void submitReservation(ReservationForm reservationForm, Users user);
    List<Map<String, String>> getReservationStatus();
    Reservations getReservationById(Long reservationId);

}
