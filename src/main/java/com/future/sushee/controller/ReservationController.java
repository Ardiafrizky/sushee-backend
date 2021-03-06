package com.future.sushee.controller;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.PriceResponse;
import com.future.sushee.payload.response.ReservationResponse;
import com.future.sushee.service.interfaces.EmailService;
import com.future.sushee.service.interfaces.ReservationService;
import com.future.sushee.service.interfaces.SeatService;
import com.future.sushee.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    private final ReservationService reservationService;
    private final EmailService emailService;
//    private final int price = 200000;
//    private final float tax = 0.1f;

    @Value("${sushee.price}")
    public int price;

    @Value("${sushee.tax}")
    public float tax;

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
        String message;
        try {
            reservation = reservationService.getById(id);
            message = reservationService.activate(reservation);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such reservation with ID " + String.valueOf(id)
            );
        } catch (RuntimeException re) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, re.getMessage()
            );
        }
        return ResponseEntity.ok().body(new MessageResponse(message));
    }

    @GetMapping("/{id}/cancel")
    public ReservationResponse cancelReservation(@PathVariable Long id) {
        return reservationService.createReservationResponse(reservationService.cancelReservation(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationCreationRequest reservationCreationRequest) {
        Reservation reservation = reservationService.addFromRequest(reservationCreationRequest);
        new Thread(() ->
                emailService.sendEmail(reservation.getUser().getEmail(), reservation.getId(), reservation.getUser().getUsername())).start();

        return ResponseEntity.ok().body(new MessageResponse(
                "Reservation for "+ reservationCreationRequest.getUsername() +" successfully created (id: "+reservation.getId()+")"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(reservationService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Reservation " + String.valueOf(id) + " successfully deleted"));
    }

    @GetMapping("/price-detail")
    public PriceResponse getPriceDetail() {
        PriceResponse response = new PriceResponse();
        response.setTax(tax);
        response.setPrice(price);
        return response;
    }
}