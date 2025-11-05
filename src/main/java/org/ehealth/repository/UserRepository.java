package org.ehealth.repository;

import org.ehealth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username for authentication
    Optional<User> findByUsername(String username);

    // Optional: find by email if you want email login
    Optional<User> findByEmail(String email);
}