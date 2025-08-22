package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    // Find by name (exact match)
    Optional<Theater> findByName(String name);

    // Find by name containing (for search functionality)
    List<Theater> findByNameContainingIgnoreCase(String name);

    // Find by address containing (for search functionality)
    List<Theater> findByAddressContainingIgnoreCase(String address);

    // Find theaters ordered by name
    List<Theater> findAllByOrderByNameAsc();

    // Find theaters ordered by creation date (newest first)
    List<Theater> findAllByOrderByCreatedAtDesc();

    // Custom query to find theaters with their screens
    @Query("SELECT t FROM Theater t LEFT JOIN FETCH t.screens WHERE t.id = :theaterId")
    Optional<Theater> findByIdWithScreens(@Param("theaterId") Long theaterId);

    // Find theaters that have screens
    @Query("SELECT DISTINCT t FROM Theater t WHERE t.screens IS NOT EMPTY")
    List<Theater> findTheatersWithScreens();

    // Find theaters by name and address
    List<Theater> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String name, String address);

    // Custom query to find theaters with screen count
    @Query("SELECT t, COUNT(s) as screenCount FROM Theater t LEFT JOIN t.screens s GROUP BY t ORDER BY screenCount DESC")
    List<Object[]> findTheatersWithScreenCount();

    // Find theaters with no screens (for cleanup)
    @Query("SELECT t FROM Theater t WHERE t.screens IS EMPTY")
    List<Theater> findTheatersWithoutScreens();

    // Find theaters by name or address containing
    @Query("SELECT t FROM Theater t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Theater> findByNameOrAddressContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}
