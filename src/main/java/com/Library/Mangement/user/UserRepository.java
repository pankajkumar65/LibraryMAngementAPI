package com.Library.Mangement.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email for authentication purposes
    User findByEmail(String email);

    // Fetch all users with the ADMIN role for random admin assignment
    List<User> findByRole(Role role);

    // Find a user by ID (Long instead of Integer since your User entity uses Long for the ID)
    Optional<User> findById(Long id);

}
