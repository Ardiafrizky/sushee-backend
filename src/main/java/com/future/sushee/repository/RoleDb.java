package com.future.sushee.repository;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDb extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(EnumRole name);
}
