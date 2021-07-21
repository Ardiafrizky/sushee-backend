package com.future.sushee.service.interfaces;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.ReservationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationService {
    List<Reservation> getAllReservation();
    List<Reservation> getReservationByUsername(String username);
    ReservationResponse createReservationResponse(Reservation reservation);
    Reservation addFromRequest(ReservationCreationRequest reservationCreationRequest);
    Reservation add(Reservation reservation);
    Reservation getById(Long id);
    Reservation deleteById(Reservation reservation);
    String activate(Reservation reservation);
    Boolean checkSeatValidity(Reservation reservation);
    Boolean checkAvailability(Reservation reservation);
    Integer calculatePrice(Integer price, Integer numberOfPerson, Float tax);
    Set<Seat> getAvailableSeats(LocalDateTime localDateTime);
}
