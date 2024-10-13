package com.bh.cwms.user.repository;

import com.bh.cwms.user.model.entity.User;
import com.bh.cwms.user.model.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByIdAndStatus(UUID id, Status status);
    Optional<User> findByUsernameAndStatus(String username, Status status);

    Optional<User> findByUsernameOrPhoneOrEmail(String username, String email, String phone);
}
