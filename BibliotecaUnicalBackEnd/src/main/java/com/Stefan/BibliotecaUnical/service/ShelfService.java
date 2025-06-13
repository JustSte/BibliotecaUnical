package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.CreateShelfDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.mapper.ShelfMapper;
import com.Stefan.BibliotecaUnical.models.Shelf;
import com.Stefan.BibliotecaUnical.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelfService {

    private final ShelfMapper shelfMapper;
    private final ShelfRepository shelfRepository;

    public List<ShelfDTO> getAllShelves()
    {
        List<ShelfDTO> shelvesDTO = shelfMapper.toDtoList(shelfRepository.findAll());
        return shelvesDTO;
    }

    public ShelfDTO getShelfById(Long id)
    {
       ShelfDTO shelf =  shelfMapper.toDTO(shelfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No shelf with id: " + id + " found.")));
       return shelf;
    }

    public List<BookSummaryDTO> getBooksOfShelf(Long id)
    {
        List<BookSummaryDTO> bookList = getShelfById(id).getBooks();
        return bookList;
    }

    @Transactional
    public ShelfDTO saveShelf(ShelfDTO ShelfDTO)
    {
        log.info("Shelf is being saved.");
        Shelf shelf = shelfMapper.toEntity(ShelfDTO);
        ShelfDTO saved = shelfMapper.toDTO(shelfRepository.save(shelf));
        return saved;
    }

    public CreateShelfDTO createShelf(CreateShelfDTO createShelfDTO)
    {
        log.info("Shelf is being created.");
        ShelfDTO shelfDTO = shelfMapper.toDTOFromCreate(createShelfDTO);
        CreateShelfDTO saved = shelfMapper.toCreateShelfDTO(saveShelf(shelfDTO));
        return saved;
    }


    public void deleteShelf(Long id)
    {
        log.info("Shelf is being deleted.");
        if(shelfRepository.existsById(id))
        {
            shelfRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Shelf with ID: " + id + " not found.");
        }
    }

}
