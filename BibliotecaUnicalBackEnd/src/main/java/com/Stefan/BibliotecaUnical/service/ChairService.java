package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.ChairFlatDTO;
import com.Stefan.BibliotecaUnical.mapper.ChairMapper;
import com.Stefan.BibliotecaUnical.models.Chair;
import com.Stefan.BibliotecaUnical.repository.ChairRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChairService {

    private final ChairRepository chairRepository;
    private final ChairMapper chairMapper;

    @Cacheable(value = "chairs", key = "#id")
    public ChairFlatDTO getChairById(Long id)
    {
        ChairFlatDTO chairFlatDTO = chairMapper.toFlatfromEntity(chairRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Chair with id: " + id + " not found!")));
        return chairFlatDTO;
    }

    public Page<ChairDTO> getAllChairs(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chair> chairPage = chairRepository.findAll(pageable);
        Page<ChairDTO> result = chairPage.map(chair -> chairMapper.toDTO(chair));
        return result;
    }


    @Transactional
    @CachePut(value = "chairs", key="#result.id")
    public ChairDTO saveChair(ChairDTO chairDTO)
    {
        log.info("Saving chair: {}", chairDTO);
        Chair chair = chairMapper.toEntity(chairDTO);
        ChairDTO saved = chairMapper.toDTO(chairRepository.save(chair));
        return saved;
    }

    @Transactional
    @CacheEvict(value = "chairs", key="#id")
    public void deleteChair(Long id)
    {
        if(chairRepository.existsById(id))
        {
            chairRepository.deleteById(id);
        }
        else
        {
            throw new ResourceNotFoundException("Chair with id: " + id + " not found.");
        }
    }
}
