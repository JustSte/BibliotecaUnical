package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.TableDTO;
import com.Stefan.BibliotecaUnical.mapper.TableMapper;
import com.Stefan.BibliotecaUnical.models.Table;
import com.Stefan.BibliotecaUnical.repository.TableRepository;
import com.Stefan.BibliotecaUnical.request.ModifyTableRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableService {

    private final TableMapper tableMapper;
    private final TableRepository tableRepository;

    public List<TableDTO> getAllTables()
    {
        List<TableDTO> tableDTOList = tableMapper.toDTOList(tableRepository.findAll());
        return tableDTOList;
    }

    public TableDTO getTableById(Long id)
    {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No table with id: " + id + " found."));
        TableDTO tableDTO = tableMapper.toDTO(table);
        return tableDTO;
    }

    @Transactional
    public TableDTO saveTable(TableDTO tableDTO)
    {
        log.info("Table {} is being saved.", tableDTO.getId());
        Table table = tableMapper.toEntity(tableDTO);
        TableDTO saved = tableMapper.toDTO(tableRepository.save(table));
        return saved;
    }

    public TableDTO updateTable(ModifyTableRequest request)
    {
        log.info("Table {} is being modified.", request.getId());
        TableDTO tableToModify = getTableById(request.getId());
        tableToModify.setName(request.getName());
        TableDTO saved = saveTable(tableToModify);
        return saved;
    }

    public void deleteTable(Long id)
    {
        log.info("Table {} is being deleted.", id);
        if(tableRepository.existsById(id))
        {
            tableRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Table with id: " + id + " not found.");
        }
    }
}
