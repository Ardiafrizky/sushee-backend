package com.future.sushee.service;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getAllReservation() { return reservationRepository.findAll(); }

    @Override
    public Reservation add(Reservation reservation) {
        reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public Reservation getById(Long id) { return reservationRepository.findById(id).get(); }

    @Override
    public Reservation deleteById(Reservation reservation) {
        reservationRepository.delete(reservation);
        return reservation;
    }

    @Override
    public Boolean checkSeatValidity(Reservation reservation) {
        Seat seat = reservation.getSeat();
        Integer person = reservation.getNumberOfPerson();
        return (seat.getCapacity() >= (person));
    }

    @Override
    public Boolean checkAvailability(Reservation reservation) {
        Long seat = reservation.getSeat().getNumber();
        LocalDateTime newStart = reservation.getStartingDateTime();
        LocalDateTime newEnd = newStart.plusMinutes(90);
        for(Reservation r : getAllReservation()){
            if(seat.equals(r.getSeat().getNumber())){
                LocalDateTime rStart = r.getStartingDateTime();
                LocalDateTime rEnd = rStart.plusMinutes(90);
                if(newEnd.isAfter(rStart) && newStart.isBefore(rEnd)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Integer calculatePrice(Integer price, Integer numberOfPerson, Float tax) {
        return Math.round(((numberOfPerson * price) * (1-tax)));
    }
}
