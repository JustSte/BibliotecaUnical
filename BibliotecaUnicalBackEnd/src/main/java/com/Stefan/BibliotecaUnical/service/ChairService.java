package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import com.Stefan.BibliotecaUnical.mapper.ChairMapper;
import com.Stefan.BibliotecaUnical.models.Chair;
import com.Stefan.BibliotecaUnical.repository.ChairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ChairDTO getChairById(Long id)
    {
        ChairDTO chairDTO = chairMapper.toDTO(chairRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Chair with id: " + id + " not found!")));
        return chairDTO;
    }

    public List<ChairDTO> getAllChairs()
    {
        List<ChairDTO> chairDTOList= chairMapper.toDtoList(chairRepository.findAll());
        return chairDTOList;
    }

    @Transactional
    public List<ChairSummaryDTO> generateChairsForTable()
    {
        List<ChairSummaryDTO> chairSummaryDTOList = new ArrayList<>();
        for(int i = 0; i<8;i++)
        {
            ChairDTO chairDTO = saveChair(new ChairDTO());
            chairSummaryDTOList.add(chairMapper.toChairSummaryDTOFromDTO(chairDTO));
        }
        return chairSummaryDTOList;
    }

    @Transactional
    public ChairDTO saveChair(ChairDTO chairDTO)
    {
        log.info("Saving chair: {}", chairDTO);
        Chair chair = chairMapper.toEntity(chairDTO);
        ChairDTO saved = chairMapper.toDTO(chairRepository.save(chair));
        return saved;
    }
}
