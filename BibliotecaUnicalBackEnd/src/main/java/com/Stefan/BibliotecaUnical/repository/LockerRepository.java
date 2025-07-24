package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.models.Locker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    List<Locker> findBySideIgnoreCaseOrderById(String side);
}
