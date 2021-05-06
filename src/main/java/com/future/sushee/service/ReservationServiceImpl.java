package com.future.sushee.service;

import com.future.sushee.model.Reservation;
import com.future.sushee.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Integer calculatePrice(Integer price, Integer numberOfPerson, Float tax) {
        return Math.round(((numberOfPerson * price) * (1-tax)));
    }
}
