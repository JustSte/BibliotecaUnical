package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.models.LibraryTable;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface LibraryTableMapper {

    LibraryTable toEntity(LibraryTableDTO libraryTableDTO);
    LibraryTableDTO toDTO(LibraryTable libraryTable);

    List<LibraryTable> toEntityList(List<LibraryTableDTO> libraryTableDTOList);
    List<LibraryTableDTO> toDTOList(List<LibraryTable> libraryTableList);

}
