package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.BookFlatDTO;
import com.Stefan.BibliotecaUnical.request.ModifyBookRequest;
import com.Stefan.BibliotecaUnical.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<BookFlatDTO>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "15") int size)
    {
        Page<BookFlatDTO> booksPage = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/withoutShelf")
    public ResponseEntity<Page<BookSummaryDTO>> getAllBooksWithoutShelf(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "15") int size)
    {
        return new ResponseEntity<>(bookService.getAllBooksWithoutShelf(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable @NotNull Long id)
    {
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/ofShelf/{id}")
    public ResponseEntity<Page<BookSummaryDTO>> getBooksOfShelf(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "15") int size,
                                                                @PathVariable @NotNull Long id) {
        Page<BookSummaryDTO> bookSummaryDTOPage = bookService.getBooksOfShelf(id, page, size);
        return ResponseEntity.ok(bookSummaryDTOPage);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO)
    {
        return new ResponseEntity<>(bookService.saveBook(bookDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody ModifyBookRequest request)
    {
        return new ResponseEntity<>(bookService.updateBook(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id)
    {
        bookService.deteleBook(id);
        return new ResponseEntity<>(("Book with id: " + id + " successfully deleted."), HttpStatus.OK);
    }
}
