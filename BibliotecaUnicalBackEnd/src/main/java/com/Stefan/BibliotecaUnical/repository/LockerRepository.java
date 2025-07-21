package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.models.Locker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerRepository extends JpaRepository<Locker, Long> {
/*    @Query("SELECT l FROM Locker l WHERE LOWER(l.side) = LOWER(:side)")
    List<Locker> findBySideIgnoreCaseOrderById(@Param("side") String side);*/
    List<Locker> findBySideIgnoreCaseOrderById(String side);
}
