package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT b
            FROM Reservation b
            WHERE b.status = 'ACTIVE'
            AND b.nextConfirmationTime <= :currentTime
            """)
    List<Reservation> findActiveReservationsNeedingConfirmation(@Param("currentTime")LocalDateTime currentTime);


}

