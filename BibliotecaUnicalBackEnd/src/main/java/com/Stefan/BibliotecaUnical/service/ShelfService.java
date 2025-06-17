package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.mapper.ShelfMapper;
import com.Stefan.BibliotecaUnical.models.Shelf;
import com.Stefan.BibliotecaUnical.repository.ShelfRepository;
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
public class ShelfService {

    private final ShelfMapper shelfMapper;
    private final ShelfRepository shelfRepository;

    public Page<ShelfDTO> getAllShelves(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shelf> shelfPage = shelfRepository.findAll(pageable);
        Page<ShelfDTO> result = shelfPage.map(shelf -> shelfMapper.toDTO(shelf));
        return result;
    }

    @Cacheable(value = "shelf", key = "#id")
    public ShelfDTO getShelfById(Long id)
    {
       Shelf shelf =  shelfRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No shelf with id: " + id + " found."));
       ShelfDTO shelfDTO = shelfMapper.toDTO(shelf);
       log.info("Returned class: {} " , shelf.getClass());
       return shelfDTO;
    }

    @Transactional
    @CachePut(value = "shelf", key="#result.id")
    public ShelfDTO saveShelf(ShelfDTO ShelfDTO)
    {
        log.info("Shelf is being saved.");
        Shelf shelf = shelfMapper.toEntity(ShelfDTO);
        ShelfDTO saved = shelfMapper.toDTO(shelfRepository.save(shelf));
        return saved;
    }

    @CacheEvict(value = "shelf", key="#id")
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
