package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.request.ModifyBookRequest;
import com.Stefan.BibliotecaUnical.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Manage books of the library")
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @GetMapping
    @Operation(summary = "Retrive all the books in db")
    public ResponseEntity<Page<BookDTO>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "15") int size)
    {
        Page<BookDTO> booksPage = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(booksPage);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/withoutShelf")
    @Operation(summary = "Retrive all the books in db without shelf's id")
    public ResponseEntity<Page<BookSummaryDTO>> getAllBooksWithoutShelf(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "15") int size)
    {
        return new ResponseEntity<>(bookService.getAllBooksWithoutShelf(page, size), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Retrive a book by it's id")
    public ResponseEntity<BookDTO> getBookById(@PathVariable @NotNull Long id)
    {
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @GetMapping("/ofShelf/{id}")
    @Operation(summary = "Get a list(page) of books from a shelf")
    public ResponseEntity<Page<BookSummaryDTO>> getBooksOfShelf(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "15") int size,
                                                                @PathVariable @NotNull Long id) {
        Page<BookSummaryDTO> bookSummaryDTOPage = bookService.getBooksOfShelf(id, page, size);
        return ResponseEntity.ok(bookSummaryDTOPage);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping
    @Operation(summary = "Creates a book")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO)
    {
        return new ResponseEntity<>(bookService.saveBook(bookDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody ModifyBookRequest request)
    {
        return new ResponseEntity<>(bookService.updateBook(id, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<String> deleteBook(@PathVariable Long id)
    {
        bookService.deteleBook(id);
        return new ResponseEntity<>(("Book with id: " + id + " successfully deleted."), HttpStatus.OK);
    }
}
