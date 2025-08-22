package com.ishan.moviereservation.service;

import com.ishan.moviereservation.entity.User;
import com.ishan.moviereservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Create a new user
     */
    public User createUser(User user) {
        log.info("Creating new user with username: {}", user.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Find user by ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Find user by username (for authentication)
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Find user by ID with their reservations
     */
    @Transactional(readOnly = true)
    public Optional<User> findByIdWithReservations(Long id) {
        log.debug("Finding user by ID with reservations: {}", id);
        return userRepository.findByIdWithReservations(id);
    }

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Finding all users");
        return userRepository.findAll();
    }

    /**
     * Find users by role
     */
    @Transactional(readOnly = true)
    public List<User> findByRole(String role) {
        log.debug("Finding users by role: {}", role);
        return userRepository.findByRole(role);
    }

    /**
     * Find users by role ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<User> findByRoleOrderByCreatedAtDesc(String role) {
        log.debug("Finding users by role ordered by creation date: {}", role);
        return userRepository.findByRoleOrderByCreatedAtDesc(role);
    }

    /**
     * Find users who have made reservations
     */
    @Transactional(readOnly = true)
    public List<User> findUsersWithReservations() {
        log.debug("Finding users with reservations");
        return userRepository.findUsersWithReservations();
    }

    /**
     * Search users by username
     */
    @Transactional(readOnly = true)
    public List<User> searchByUsername(String username) {
        log.debug("Searching users by username: {}", username);
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    /**
     * Search users by email
     */
    @Transactional(readOnly = true)
    public List<User> searchByEmail(String email) {
        log.debug("Searching users by email: {}", email);
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    /**
     * Update user
     */
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found with ID: " + user.getId());
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    /**
     * Delete user by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    /**
     * Check if username exists
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        log.debug("Checking if username exists: {}", username);
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * Get user count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total user count");
        return userRepository.count();
    }

    /**
     * Get user count by role
     */
    @Transactional(readOnly = true)
    public long countByRole(String role) {
        log.debug("Getting user count by role: {}", role);
        return userRepository.findByRole(role).size();
    }
}
