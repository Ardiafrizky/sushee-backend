package com.future.sushee.repository;

import com.future.sushee.model.SeatModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatDb extends JpaRepository<SeatModel, Long> {
    Optional<SeatModel> findByNumber(Long number);
}
