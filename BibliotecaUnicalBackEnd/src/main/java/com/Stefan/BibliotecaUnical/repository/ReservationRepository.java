package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.DTO.ReservationDTOs.ReservationDTO;
import com.Stefan.BibliotecaUnical.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT b
            FROM Reservation b
            WHERE (b.status = 'ACTIVE' OR b.status = 'PENDING_CONFIRMATION')
            AND b.nextConfirmationTime <= :currentTime
            """)
    List<Reservation> findActiveReservationsNeedingConfirmation(@Param("currentTime")LocalDateTime currentTime);

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE (r.status = 'ACTIVE' OR r.status = 'PENDING_CONFIRMATION')
            AND r.userId = :userId
            AND r.resourceType = :resourceType
            """)
    Reservation findActiveReservationForUser(@Param("userId") String userId, @Param("resourceType") Reservation.ResourceType resourceType);


    @Query("""
            SELECT b
            FROM Reservation b
            WHERE (b.status = 'PENDING_CONFIRMATION')
            AND b.startTime <= :timeMinus
            """)
    Reservation findReservationToExpire(@Param("timeMinus") LocalDateTime timeMinus);

    Optional<Reservation> findByUserId(String id);
}

