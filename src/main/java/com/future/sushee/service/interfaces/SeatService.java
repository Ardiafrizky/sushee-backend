package com.future.sushee.service.interfaces;

import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.SeatCreationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SeatService {
    List<Seat> getAllSeat();
    Seat getByNumber(Long number);
    Seat add(Seat seat);
    Seat addFromRequest(SeatCreationRequest seatCreationRequest);
    Seat delete(Seat seat);
}
