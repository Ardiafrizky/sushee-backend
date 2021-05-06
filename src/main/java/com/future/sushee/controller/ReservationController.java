package com.future.sushee.controller;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.LoginRequest;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.repository.ReservationRepository;
import com.future.sushee.repository.RoleRepository;
import com.future.sushee.repository.UserRepository;
import com.future.sushee.security.jwt.JwtUtils;
import com.future.sushee.service.ReservationService;
import com.future.sushee.service.SeatService;
import com.future.sushee.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final SeatService seatService;

    @PostMapping("/add")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody ReservationCreationRequest reservationCreationRequest) {
//        try {
            Reservation reservation = new Reservation();

            reservation.setNumberOfPerson(reservationCreationRequest.getNumberOfPerson());
            reservation.setStartingDateTime(reservationCreationRequest.getStartingDateTime());
            reservation.setUser(userService.getUserByUsername(reservationCreationRequest.getUsername()));
            reservation.setStatus(reservationCreationRequest.getStatus());
            reservation.setTotalPrice(reservationService.calculatePrice(200000, reservationCreationRequest.getNumberOfPerson(), 0.1f));

            if (reservationCreationRequest.getNumberOfPerson() <= seatService.getByNumber(reservationCreationRequest.getSeatNumber()).getCapacity()) {
                reservation.setSeat(seatService.getByNumber(reservationCreationRequest.getSeatNumber()));
            } else { throw new RuntimeException("Error: Invalid seat capacity"); }

            reservationService.add(reservation);
            return ResponseEntity.ok().body(new MessageResponse("Reservation successfully created."));
//        } catch (Exception exception) {
//            throw new RuntimeException("Error: Failed creating reservation, \nLog: " + exception.getMessage());
//        }
    }
}