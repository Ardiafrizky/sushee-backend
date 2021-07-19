package com.future.sushee.service;

import com.future.sushee.model.Reservation;
import com.future.sushee.payload.response.ReservationResponse;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> getAllReservation();
    List<Reservation> getReservationByUsername(String username);
    ReservationResponse createReservationResponse(Reservation reservation);
    Reservation add(Reservation reservation);
    Reservation getById(Long id);
    Reservation deleteById(Reservation reservation);
    String activate(Reservation reservation);
    Boolean checkSeatValidity(Reservation reservation);
    Boolean checkAvailability(Reservation reservation);
    Integer calculatePrice(Integer price, Integer numberOfPerson, Float tax);
}
