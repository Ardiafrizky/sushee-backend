package com.future.sushee.service;

import com.future.sushee.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> getAllReservation();
    Reservation add(Reservation reservation);
    Reservation getById(Long id);
    Reservation deleteById(Reservation reservation);
    Integer calculatePrice(Integer price, Integer numberOfPerson, Float tax);
}
