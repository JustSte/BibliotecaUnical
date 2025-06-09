package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.CreateShelfDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.mapper.BookMapper;
import com.Stefan.BibliotecaUnical.mapper.ShelfMapper;
import com.Stefan.BibliotecaUnical.models.Shelf;
import com.Stefan.BibliotecaUnical.repository.BookRepository;
import com.Stefan.BibliotecaUnical.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfMapper shelfMapper;
    private final BookMapper bookMapper;
    private final ShelfRepository shelfRepository;
    private final BookRepository bookRepository;

    public List<ShelfDTO> getAllShelves()
    {
        List<ShelfDTO> shelvesDTO = shelfMapper.toDtoList(shelfRepository.findAll());
        return shelvesDTO;
    }


    @Transactional
    public ShelfDTO saveShelf(ShelfDTO ShelfDTO)
    {
        Shelf shelf = shelfMapper.toEntity(ShelfDTO);
        ShelfDTO saved = shelfMapper.toDTO(shelfRepository.save(shelf));
        return saved;
    }

    public CreateShelfDTO createShelf(CreateShelfDTO createShelfDTO)
    {
        ShelfDTO shelfDTO = shelfMapper.toDTOFromCreate(createShelfDTO);
        CreateShelfDTO saved = shelfMapper.toCreateShelfDTO(saveShelf(shelfDTO));
        return saved;
    }

    public ShelfDTO addBooksToShelf(Long id , List<Long> bookList)
    {
        ShelfDTO shelfDTO = shelfMapper.toDTO(shelfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shelf not found")));

        List<BookSummaryDTO> booksToAdd = bookMapper.toSummaryDTOList(bookRepository.findAllById(bookList));
        List<BookSummaryDTO> existingBooks = shelfDTO.getBooks();

        for(BookSummaryDTO book : booksToAdd)
        {
            if(!existingBooks.contains(book.getId())){
                shelfDTO.getBooks().add(book);
            }
        }

        return saveShelf(shelfDTO);
    }

}
