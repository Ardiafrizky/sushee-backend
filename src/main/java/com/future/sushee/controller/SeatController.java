package com.future.sushee.controller;

import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.SeatCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.service.interfaces.SeatService;
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
@RequestMapping("/api/seat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SeatController {
    
    private final SeatService seatService;
    
    @GetMapping("")
    public List<Seat> getAllSeat() {
        return seatService.getAllSeat();
    }

    @GetMapping("/{number}")
    public Seat getSeatById(@PathVariable Long number) {
        try {
            return seatService.getByNumber(number);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such seat with ID " + String.valueOf(number)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSeat(@Valid @RequestBody SeatCreationRequest seatCreationRequest) {
        Seat seat = seatService.addFromRequest(seatCreationRequest);
        return ResponseEntity.ok().body(new MessageResponse("Seat "+ seat.getNumber() +" successfully added."));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long number) {
        seatService.delete(seatService.getByNumber(number));
        return ResponseEntity.ok(new MessageResponse("Seat " + String.valueOf(number) + " successfully deleted"));
    }
}
