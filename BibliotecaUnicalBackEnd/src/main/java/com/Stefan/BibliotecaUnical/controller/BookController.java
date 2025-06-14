package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.request.ModifyBookRequest;
import com.Stefan.BibliotecaUnical.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks()
    {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/withoutShelf")
    public ResponseEntity<List<BookSummaryDTO>> getAllBooksWithoutShelf()
    {
        return new ResponseEntity<>(bookService.getAllBooksWithoutShelf(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO)
    {
        return new ResponseEntity<>(bookService.saveBook(bookDTO), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<BookSummaryDTO> updateBook(@Valid @RequestBody ModifyBookRequest request)
    {
        return new ResponseEntity<>(bookService.updateBook(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id)
    {
        bookService.deteleBook(id);
        return new ResponseEntity<>(("Book with id: " + id + " successfully deleted."), HttpStatus.OK);
    }
}
