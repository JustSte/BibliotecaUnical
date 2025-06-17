package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.LibraryTableFlatDTO;
import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.mapper.LibraryTableMapper;
import com.Stefan.BibliotecaUnical.models.LibraryTable;
import com.Stefan.BibliotecaUnical.repository.LibraryTableRepository;
import com.Stefan.BibliotecaUnical.request.ModifyTableRequest;
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
@Slf4j
@RequiredArgsConstructor
public class LibraryTableService {

    private final LibraryTableMapper libraryTableMapper;
    private final LibraryTableRepository libraryTableRepository;

    public Page<LibraryTableDTO> getAllTables(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<LibraryTable> libraryTablePage = libraryTableRepository.findAll(pageable);
        Page<LibraryTableDTO> result = libraryTablePage.map(libraryTable -> libraryTableMapper.toDTO(libraryTable));
        return result;
    }

    public List<LibraryTableFlatDTO> getAllTablesList()
    {
        List<LibraryTableFlatDTO> tableDTOList = libraryTableMapper.toFlatDTOListFromEntity(libraryTableRepository.findAll());
        return tableDTOList;
    }

    @Cacheable(value = "libraryTable", key = "#id")
    public LibraryTableDTO getTableById(Long id)
    {
        LibraryTable libraryTable = libraryTableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No libraryTable with id: " + id + " found."));
        LibraryTableDTO libraryTableDTO = libraryTableMapper.toDTO(libraryTable);
        return libraryTableDTO;
    }

    public List<ChairSummaryDTO> getChairsOfTable(Long id)
    {
        List<ChairSummaryDTO> chairSummaryDTOList = getTableById(id).getChairs();
        return chairSummaryDTOList;
    }


    @Transactional
    @CachePut(value = "libraryTable", key = "#result.id")
    public LibraryTableDTO saveTable(LibraryTableDTO libraryTableDTO)
    {
        log.info("LibraryTable {} is being saved.", libraryTableDTO.getId());
        LibraryTable libraryTable = libraryTableMapper.toEntity(libraryTableDTO);
        LibraryTableDTO saved = libraryTableMapper.toDTO(libraryTableRepository.save(libraryTable));
        return saved;
    }

    @CachePut(value = "libraryTable", key = "#result.id")
    public LibraryTableDTO updateTable(ModifyTableRequest request, Long id)
    {
        log.info("LibraryTable {} is being modified.", id);
        LibraryTableDTO tableToModify = getTableById(id);
        tableToModify.setName(request.getName());
        tableToModify.setPositionX(request.getPositionX());
        tableToModify.setPositionY(request.getPositionY());
        LibraryTableDTO saved = saveTable(tableToModify);
        return saved;
    }

    @CacheEvict(value = "libraryTable", key = "#id")
    public void deleteTable(Long id)
    {
        log.info("LibraryTable {} is being deleted.", id);
        if(libraryTableRepository.existsById(id))
        {
            libraryTableRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("LibraryTable with id: " + id + " not found.");
        }
    }
}
