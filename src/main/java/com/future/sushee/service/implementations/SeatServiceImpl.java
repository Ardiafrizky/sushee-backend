package com.future.sushee.service.implementations;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.SeatCreationRequest;
import com.future.sushee.repository.SeatRepository;
import com.future.sushee.service.interfaces.ReservationService;
import com.future.sushee.service.interfaces.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<Seat> getAllSeat() { return seatRepository.findAll(); }

    @Override
    public Seat add(Seat seat) {
        seatRepository.save(seat);
        return seat;
    }

    @Override
    public Seat addFromRequest(SeatCreationRequest seatCreationRequest) {
        Seat seat = new Seat();
        seat.setNumber(seatCreationRequest.getNumber());
        seat.setAvailable(seatCreationRequest.getAvailable());
        seat.setCapacity(seatCreationRequest.getCapacity());

        return add(seat);
    }

    @Override
    public Seat getByNumber(Long number) { return seatRepository.findByNumber(number).get(); }

    @Override
    public Seat delete(Seat seat) {
        seatRepository.delete(seat);
        return seat;
    }
}
