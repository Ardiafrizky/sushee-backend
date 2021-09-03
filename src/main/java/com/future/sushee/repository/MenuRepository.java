package com.future.sushee.repository;

import com.future.sushee.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findById(Long id);
    Optional<Menu> findMenuByName(String name);
}
