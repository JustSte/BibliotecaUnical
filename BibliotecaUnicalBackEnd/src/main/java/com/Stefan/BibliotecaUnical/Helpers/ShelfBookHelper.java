package com.Stefan.BibliotecaUnical.Helpers;

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
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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

    public ShelfDTO addBooksToShelf(Long id , List<Long> bookList)
    {
        ShelfDTO shelfDTO = shelfMapper.toDTO(shelfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shelf not found")));

        List<BookSummaryDTO> booksToAdd = bookMapper.toSummaryDTOList(bookRepository.findAllById(bookList));
        List<BookSummaryDTO> existingBooks = shelfDTO.getBooks();

        for(BookSummaryDTO book : booksToAdd)
        {
            if(!existingBooks.contains(book.getId())){
                log.info("Book is being added to shelf.");
                shelfDTO.getBooks().add(book);
                BookDTO bookDTO = bookMapper.toDTOFromSummary(book);
                bookDTO.setShelfID(shelfDTO.getId());
                bookService.saveBook(bookDTO);
            }
        }
        return shelfService.saveShelf(shelfDTO);
    }



}
