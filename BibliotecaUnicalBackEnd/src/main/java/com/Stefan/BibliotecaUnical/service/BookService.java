package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.mapper.BookMapper;
import com.Stefan.BibliotecaUnical.models.Book;
import com.Stefan.BibliotecaUnical.repository.BookRepository;
import com.Stefan.BibliotecaUnical.request.ModifyBookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public List<BookDTO> getAllBooks()
    {
        List<BookDTO> bookDTOList = bookMapper.toDtoList(bookRepository.findAll());
        return bookDTOList;
    }

    public List<BookSummaryDTO> getAllBooksWithoutShelf()
    {
        List<BookSummaryDTO> bookSummaryDTOList = bookMapper.toSummaryDTOList(bookRepository.findAll());
        return bookSummaryDTOList;
    }

    @Transactional
    public BookDTO saveBook(BookDTO bookDTO)
    {
        log.info("The book is being saved.");
        Book book = bookMapper.toEntity(bookDTO);
        BookDTO saved = bookMapper.toDTO(bookRepository.save(book));
        return saved;
    }

    public void deteleBook(Long id)
    {
        if(bookRepository.existsById(id))
        {
            bookRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Book with ID: " + id + " not found.");
        }
    }

    public BookSummaryDTO updateBook(ModifyBookRequest request)
    {
        Book bookToModify = bookRepository.findById(request.getBookId()).orElseThrow(() -> new ResourceNotFoundException("Shelf not found"));
        BookDTO book = bookMapper.toDTO(bookToModify);
        book.setTitle(request.getTitle());
        BookSummaryDTO updatedBook = bookMapper.toSummaryDTOFromDTO(saveBook(book));
        return updatedBook;

    }

}
