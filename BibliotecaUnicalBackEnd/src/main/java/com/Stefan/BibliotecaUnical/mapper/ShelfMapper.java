package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.CreateShelfDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.models.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface ShelfMapper {

    Shelf toEntity(ShelfDTO shelfDTO);
    ShelfDTO toDTO(Shelf shelf);

    ShelfDTO toDTOFromCreate(CreateShelfDTO createShelfDTO);
    CreateShelfDTO toCreateShelfDTO(ShelfDTO shelfDTO);


    List<ShelfDTO> toDtoList(List<Shelf> shelfList);
    List<Shelf> toEntityList(List<ShelfDTO> shelfDTOList);
}
