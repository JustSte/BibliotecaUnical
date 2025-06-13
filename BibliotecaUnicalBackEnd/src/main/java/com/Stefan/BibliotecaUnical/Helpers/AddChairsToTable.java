package com.Stefan.BibliotecaUnical.Helpers;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.mapper.ChairMapper;
import com.Stefan.BibliotecaUnical.service.ChairService;
import com.Stefan.BibliotecaUnical.service.LibraryTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class AddChairsToTable {

    private final LibraryTableService libraryTableService;
    private final ChairService chairService;
    private final ChairMapper chairMapper;

    @Transactional
    public LibraryTableDTO addChairsToTable(Long id, List<Long> chairIds)
    {
        LibraryTableDTO libraryTableDTO = libraryTableService.getTableById(id);
        List<ChairSummaryDTO> chairsToAdd = chairService.generateChairsForTable();

        for(ChairSummaryDTO chair : chairsToAdd)
        {
            log.info("Chair {} is being added to table {}.", chair.getId(), libraryTableDTO.getId());
            libraryTableDTO.getChairs().add(chair);
            ChairDTO chairDTO = chairMapper.toDTOFromSummaryDTO(chair);
            chairDTO.setTableId(libraryTableDTO.getId());
            chairService.saveChair(chairDTO);
        }
        return libraryTableService.saveTable(libraryTableDTO);
    }
}
