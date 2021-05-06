package com.future.sushee.repository;

import com.future.sushee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
