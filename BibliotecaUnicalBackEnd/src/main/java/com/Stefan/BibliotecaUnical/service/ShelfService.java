package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTO;
import com.Stefan.BibliotecaUnical.mapper.ShelfMapper;
import com.Stefan.BibliotecaUnical.models.Shelf;
import com.Stefan.BibliotecaUnical.repository.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfMapper shelfMapper;
    private final ShelfRepository shelfRepository;

    public List<ShelfDTO> getAllShelves()
    {
        List<ShelfDTO> shelvesDTO = shelfMapper.toDtoList(shelfRepository.findAll());
        return shelvesDTO;
    }

    @Transactional
    public ShelfDTO saveShelf(ShelfDTO shelfDTO)
    {
        Shelf shelf = shelfMapper.toEntity(shelfDTO);
        ShelfDTO saved = shelfMapper.toDTO(shelfRepository.save(shelf));
        return shelfDTO;
    }

}
