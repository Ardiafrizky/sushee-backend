package com.future.sushee.repository;

import com.future.sushee.model.MenuModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuDb extends JpaRepository<MenuModel, Long> {
    Optional<MenuModel> findById(Long id);
}
