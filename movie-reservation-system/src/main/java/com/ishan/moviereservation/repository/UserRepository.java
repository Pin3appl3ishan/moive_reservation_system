package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username (for authentication)
    Optional<User> findByUsername(String username);

    // Find by email (for authentication and validation)
    Optional<User> findByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find users by role
    List<User> findByRole(String role);

    // Find users by role with pagination
    List<User> findByRoleOrderByCreatedAtDesc(String role);

    // Custom query to find users with their reservation count
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.reservations WHERE u.id = :userId")
    Optional<User> findByIdWithReservations(@Param("userId") Long userId);

    // Find users who have made reservations
    @Query("SELECT DISTINCT u FROM User u WHERE u.reservations IS NOT EMPTY")
    List<User> findUsersWithReservations();

    // Find users by username containing (for search functionality)
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Find users by email containing (for search functionality)
    List<User> findByEmailContainingIgnoreCase(String email);
}
