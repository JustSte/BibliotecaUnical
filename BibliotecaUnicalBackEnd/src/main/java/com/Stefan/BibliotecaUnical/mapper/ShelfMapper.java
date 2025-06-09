package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTO;
import com.Stefan.BibliotecaUnical.models.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface ShelfMapper {

    ShelfMapper INSTANCE = Mappers.getMapper(ShelfMapper.class);

    Shelf toEntity(ShelfDTO shelfDTO);

    ShelfDTO toDTO(Shelf shelf);

    List<ShelfDTO> toDtoList(List<Shelf> shelfList);
    List<Shelf> toEntityList(List<ShelfDTO> shelfDTOList);


}
