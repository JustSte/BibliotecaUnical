package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.models.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    Optional<Shelf> findById(Long id);
}
