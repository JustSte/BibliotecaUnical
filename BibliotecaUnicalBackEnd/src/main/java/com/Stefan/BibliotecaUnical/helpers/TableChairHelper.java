package com.Stefan.BibliotecaUnical.helpers;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.mapper.ChairMapper;
import com.Stefan.BibliotecaUnical.service.ChairService;
import com.Stefan.BibliotecaUnical.service.LibraryTableService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TableChairHelper {

    private final LibraryTableService libraryTableService;
    private final ChairService chairService;
    private final ChairMapper chairMapper;

    @Transactional
    @Cacheable(value = "libraryTable", key = "#id")
    public List<ChairSummaryDTO> generateChairsForTable()
    {
        List<ChairSummaryDTO> chairSummaryDTOList = new ArrayList<>();
        for(int i = 0; i<8;i++)
        {
            ChairDTO chairDTO = chairService.saveChair(new ChairDTO());
            chairSummaryDTOList.add(chairMapper.toChairSummaryDTOFromDTO(chairDTO));
        }
        return chairSummaryDTOList;
    }

    @Transactional
    @CachePut(value = "libraryTable", key = "#result.id")
    public LibraryTableDTO addChairsToTable(Long id)
    {
        LibraryTableDTO libraryTableDTO = libraryTableService.getTableById(id);
        List<ChairSummaryDTO> chairsToAdd = generateChairsForTable();

        for(ChairSummaryDTO chair : chairsToAdd)
        {
            log.info("Chair {} is being added to table {}.", chair.getId(), libraryTableDTO.getId());
            libraryTableDTO.getChairs().add(chair);
            ChairDTO chairDTO = chairMapper.toDTOFromSummaryDTO(chair);
            chairDTO.setLibraryTableId(libraryTableDTO.getId());
            chairService.saveChair(chairDTO);
        }
        return libraryTableService.saveTable(libraryTableDTO);
    }

    @Transactional
    @CacheEvict(value = "libraryTable", key = "#id")
    public void deleteChairsOfTable(Long id)
    {
        log.info("Deleting chairs of table {}.", id);
        LibraryTableDTO tableDTO = libraryTableService.getTableById(id);
        List<ChairSummaryDTO> chairSummaryDTO = tableDTO.getChairs();
        for(ChairSummaryDTO chair : chairSummaryDTO)
        {
            chairService.deleteChair(chair.getId());
        }
    }
}
