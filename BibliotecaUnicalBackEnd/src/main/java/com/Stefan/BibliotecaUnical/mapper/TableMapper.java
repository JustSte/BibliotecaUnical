package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.TableDTO;
import com.Stefan.BibliotecaUnical.models.Table;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TableMapper {

    Table toEntity(TableDTO tableDTO);

    TableDTO toDTO(Table table);

    List<Table> toEntityList(List<TableDTO> tableDTOList);
    List<TableDTO> toDTOList(List<Table> tableList);
}
