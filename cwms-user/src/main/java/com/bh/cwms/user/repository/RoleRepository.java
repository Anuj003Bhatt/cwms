package com.bh.cwms.user.repository;

import com.bh.cwms.user.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Page<Role> findByNameContaining(String name, Pageable pageable);
    Optional<Role> findByName(String name);
}
