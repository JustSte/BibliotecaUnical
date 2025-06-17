package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.ChairFlatDTO;
import com.Stefan.BibliotecaUnical.models.Chair;
import com.Stefan.BibliotecaUnical.models.LibraryTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ChairMapper {

    @Mapping(target = "libraryTable", expression = "java(mapTableFromId(chairDTO.getTableId()))")
    Chair toEntity(ChairDTO chairDTO);

    @Mapping( source = "libraryTable.id", target = "tableId")
    ChairDTO toDTO(Chair chair);
    ChairDTO toDTOFromSummaryDTO(ChairSummaryDTO chairSummaryDTO);
    ChairDTO toDTOFromFlat(ChairFlatDTO chairFlatDTO);

    ChairSummaryDTO toChairSummaryDTOFromDTO(ChairDTO chairDTO);
    ChairSummaryDTO toChairSummaryDTOFromEntity(Chair chair);
    List<ChairSummaryDTO> toChairSummaryDTOList(List<Chair> chairList);
    List<ChairSummaryDTO> toChairSummaryDTOListFromDTO(List<ChairDTO> chairList);

    @Mapping(source="libraryTable.id", target = "libraryTableId")
    ChairFlatDTO toFlatFromEntity(Chair chair);
    Chair toEntityFromFlat(ChairFlatDTO chairFlatDTO);

    List<Chair> toEntityList(List<ChairDTO> chairDTOList);
    List<ChairDTO> toDTOList(List<Chair> chairList);

    default LibraryTable mapTableFromId(Long tableId)
    {
        if(tableId == null) return null;
        LibraryTable libraryTable = new LibraryTable();
        libraryTable.setId(tableId);
        return libraryTable;
    }
}
