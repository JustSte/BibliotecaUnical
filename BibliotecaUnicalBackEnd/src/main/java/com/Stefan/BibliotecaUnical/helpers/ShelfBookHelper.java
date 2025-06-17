package com.Stefan.BibliotecaUnical.helpers;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.mapper.BookMapper;
import com.Stefan.BibliotecaUnical.mapper.ShelfMapper;
import com.Stefan.BibliotecaUnical.repository.BookRepository;
import com.Stefan.BibliotecaUnical.repository.ShelfRepository;
import com.Stefan.BibliotecaUnical.service.BookService;
import com.Stefan.BibliotecaUnical.service.ShelfService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ShelfBookHelper {

    private final ShelfMapper shelfMapper;
    private final BookMapper bookMapper;
    private final ShelfRepository shelfRepository;
    private final BookRepository bookRepository;
    private final ShelfService shelfService;
    private final BookService bookService;

    @CachePut(value = "shelf", key = "#id")
    public ShelfDTO addBooksToShelf(Long id , List<Long> bookList)
    {
        ShelfDTO shelfDTO = shelfService.getShelfById(id);
        List<BookDTO> booksToAdd = bookService.getAllBooksList(bookList);
        List<BookDTO> existingBooks = shelfDTO.getBooks();

        for(BookDTO book : booksToAdd)
        {
            if(!existingBooks.contains(book)){
                log.info("Book is being added to shelf.");
                shelfDTO.getBooks().add(book);
                book.setShelfID(shelfDTO.getId());
                bookService.saveBook(book);
            }

        }
        return shelfService.saveShelf(shelfDTO);
    }

    @CachePut(value = "shelf", key = "#id")
    public ShelfDTO deleteBooksFromShelf(Long id, List<Long> books)
    {
        ShelfDTO shelfDTO = shelfService.getShelfById(id);
        List<BookDTO> booksToDelete = bookService.getAllBooksList(books);
        List<BookDTO> existingBooks = shelfDTO.getBooks();
        for(BookDTO book : booksToDelete)
        {
            if(existingBooks.contains(book))
            {
                log.info("Deleting book {} from shelf {}.", book.getId(), shelfDTO.getId());
                shelfDTO.getBooks().remove(book);
                book.setShelfID(null);
                bookService.saveBook(book);
            }
        }
        return shelfService.saveShelf(shelfDTO);
    }



}
