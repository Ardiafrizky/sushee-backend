package com.future.sushee.repository;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findById(Long id);
    List<Reservation> findByUser(User user);
}
