package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.BookFlatDTO;
import com.Stefan.BibliotecaUnical.mapper.BookMapper;
import com.Stefan.BibliotecaUnical.models.Book;
import com.Stefan.BibliotecaUnical.repository.BookRepository;
import com.Stefan.BibliotecaUnical.request.ModifyBookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<BookFlatDTO> getAllBooks(int page, int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> booksPage = bookRepository.findAll(pageable);
        Page<BookFlatDTO> result = booksPage.map(book -> bookMapper.toFlatFromEntity(book));
        return result;
    }

    public Page<BookSummaryDTO> getAllBooksWithoutShelf(int page, int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> booksPage = bookRepository.findAll(pageable);
        Page<BookSummaryDTO> result = booksPage.map(book -> bookMapper.toSummaryDTO(book));
        return result;
    }

    @Cacheable(value = "book", key = "#id")
    public BookDTO getBookById(Long id)
    {
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No book with id: " + id + " found."));
        BookDTO bookDTO = bookMapper.toDTO(book);
        return bookDTO;
    }

    @CachePut(value = "book", key = "#result.id")
    @Transactional
    public BookDTO saveBook(BookDTO bookDTO)
    {
        Book book = bookMapper.toEntity(bookDTO);
        BookDTO saved = bookMapper.toDTO(bookRepository.save(book));
        log.info("Saved book with id: {}.", saved.getId());
        return saved;
    }

    @CacheEvict(value = "book", key = "#id")
    public void deteleBook(Long id)
    {
        if(bookRepository.existsById(id))
        {
            bookRepository.deleteById(id);
        }
        else
        {
            throw new ResourceNotFoundException("Book with ID: " + id + " not found.");
        }
    }

    public Page<BookSummaryDTO> getBooksOfShelf(Long shelfId, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByShelfId(shelfId, pageable);
        Page<BookSummaryDTO> result = bookPage.map(book -> bookMapper.toSummaryDTO(book));
        return result;
    }

    @CachePut(value = "book", key ="#bookId")
    public BookDTO updateBook(Long bookId, ModifyBookRequest request)
    {
        Book bookToModify = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Shelf not found"));
        BookDTO book = bookMapper.toDTO(bookToModify);
        book.setTitle(request.getTitle());
        BookDTO updatedBook = saveBook(book);
        return updatedBook;

    }

    public List<BookDTO> getAllBooksList(List<Long> bookIds)
    {
        List<BookDTO> bookDTOList = bookMapper.toDTOList(bookRepository.findAllById(bookIds));
        return bookDTOList;
    }
}
