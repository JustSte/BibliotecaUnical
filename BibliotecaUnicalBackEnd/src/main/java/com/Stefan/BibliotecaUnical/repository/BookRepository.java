package com.Stefan.BibliotecaUnical.repository;

import com.Stefan.BibliotecaUnical.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
