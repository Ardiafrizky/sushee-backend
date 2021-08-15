package com.future.sushee.controller;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.ReservationResponse;
import com.future.sushee.service.interfaces.EmailService;
import com.future.sushee.service.interfaces.ReservationService;
import com.future.sushee.service.interfaces.SeatService;
import com.future.sushee.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    private final ReservationService reservationService;
    private final EmailService emailService;

    @GetMapping("")
    public List<ReservationResponse> getAllReservation() {
        List<Reservation> reservations = reservationService.getAllReservation();
        List<ReservationResponse> response = new ArrayList<>();
        for (Reservation reservation: reservations) {
            response.add(reservationService.createReservationResponse(reservation));
        }
        return response;
    }

    @GetMapping("/user/{username}")
    public List<ReservationResponse> getReservationByUser(@PathVariable String username) {
        List<Reservation> reservations = reservationService.getReservationByUsername(username);
        List<ReservationResponse> response = new ArrayList<>();
        for (Reservation reservation: reservations) {
            response.add(reservationService.createReservationResponse(reservation));
        }
        return response;
    }

    @GetMapping("/id/{id}")
    public ReservationResponse getReservationById(@PathVariable Long id) {
        ReservationResponse response = new ReservationResponse();
        try {
            Reservation reservation = reservationService.getById(id);
            return reservationService.createReservationResponse(reservation);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such reservation with ID " + String.valueOf(id)
            );
        }
    }

    @GetMapping("/{id}/update-status")
    public ResponseEntity<?> activateReservation(@PathVariable Long id) {
        Reservation reservation;
        try {
            reservation = reservationService.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such reservation with ID " + String.valueOf(id)
            );
        }
        String message = reservationService.activate(reservation);
        return ResponseEntity.ok().body(new MessageResponse(message));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationCreationRequest reservationCreationRequest) {
        Reservation reservation = reservationService.addFromRequest(reservationCreationRequest);
        emailService.sendEmail(reservation.getUser().getEmail(), reservation.getId(), reservation.getUser().getUsername());
        return ResponseEntity.ok().body(new MessageResponse(
                "Reservation for "+ reservationCreationRequest.getUsername() +" successfully created (id: "+reservation.getId()+")"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(reservationService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Reservation " + String.valueOf(id) + " successfully deleted"));
    }
}