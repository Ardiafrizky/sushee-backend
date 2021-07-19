package com.future.sushee.service.implementations;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.payload.response.ReservationResponse;
import com.future.sushee.repository.ReservationRepository;
import com.future.sushee.service.interfaces.ReservationService;
import com.future.sushee.service.interfaces.UserService;
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
    private final UserService userService;

    @Override
    public List<Reservation> getAllReservation() { return reservationRepository.findAll(); }

    @Override
    public List<Reservation> getReservationByUsername(String username) {
        return reservationRepository.findByUser(userService.getUserByUsername(username));
    }

    @Override
    public ReservationResponse createReservationResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setNumberOfPerson(reservation.getNumberOfPerson());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setStartingDateTime(reservation.getStartingDateTime());
        response.setStatus(reservation.getStatus());
        response.setSeat(reservation.getSeat().getNumber());
        response.setUser(reservation.getUser().getUuid());
        return response;
    }

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
    public String activate(Reservation reservation) {
        LocalDateTime start = reservation.getStartingDateTime();
        LocalDateTime end = start.plusMinutes(90);
        LocalDateTime now = LocalDateTime.now();

        if (reservation.getStatus()==-1) return "The reservation are already cancelled";
        if (now.isBefore(start)) return "The reservation is not started yet";
        if (now.isAfter(end)) {
            if (reservation.getStatus()==0) {
                reservation.setStatus(3);
                reservationRepository.save(reservation);
            }
            return "The reservation is expired";
        }
        else {
            reservation.setStatus(1);
            reservationRepository.save(reservation);
            return "The reservation has been activated successfully";
        }
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
