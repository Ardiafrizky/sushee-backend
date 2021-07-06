package com.future.sushee.controller;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.service.ReservationService;
import com.future.sushee.service.SeatService;
import com.future.sushee.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final SeatService seatService;

    @GetMapping("")
    public List<Reservation> getAllReservation() {
        return reservationService.getAllReservation();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        try {
            return reservationService.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such reservation with ID " + String.valueOf(id)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationCreationRequest reservationCreationRequest) {

        Reservation reservation = new Reservation();
        Seat seat = seatService.getByNumber(reservationCreationRequest.getSeatNumber());

        reservation.setNumberOfPerson(reservationCreationRequest.getNumberOfPerson());
        reservation.setUser(userService.getUserByUsername(reservationCreationRequest.getUsername()));
        reservation.setStartingDateTime(reservationCreationRequest.getStartingDateTime());
        reservation.setStatus(reservationCreationRequest.getStatus());
        reservation.setTotalPrice(reservationService.calculatePrice(200000, reservationCreationRequest.getNumberOfPerson(), 0.1f));
        reservation.setSeat(seat);

        if(!reservationService.checkSeatValidity(reservation)) {
            throw new RuntimeException("Error: Seat-Reservation constraint violation (capacity/availability)");
        }

        if (!reservationService.checkAvailability(reservation)) {
            throw new RuntimeException("Error: Booking datetime collision");
        }

        reservationService.add(reservation);
        return ResponseEntity.ok().body(new MessageResponse("Reservation successfully created."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(reservationService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Reservation " + String.valueOf(id) + " successfully deleted"));
    }
}