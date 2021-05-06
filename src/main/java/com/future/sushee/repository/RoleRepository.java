package com.future.sushee.repository;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(EnumRole name);
}
