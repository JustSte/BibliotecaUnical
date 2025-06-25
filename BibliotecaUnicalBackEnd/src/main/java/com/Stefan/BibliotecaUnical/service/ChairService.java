package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.mapper.ChairMapper;
import com.Stefan.BibliotecaUnical.models.Chair;
import com.Stefan.BibliotecaUnical.repository.ChairRepository;
import com.Stefan.BibliotecaUnical.request.ModifyChairRequest;
import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChairService {

    private final ChairRepository chairRepository;
    private final ChairMapper chairMapper;

    @Cacheable(value = "chair", key = "#id")
    public ChairDTO getChairById(Long id)
    {
        ChairDTO chairDTO = chairMapper.toDTO(chairRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Chair with id: " + id + " not found!")));
        return chairDTO;
    }

    public Page<ChairDTO> getAllChairs(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chair> chairPage = chairRepository.findAll(pageable);
        Page<ChairDTO> result = chairPage.map(chair -> chairMapper.toDTO(chair));
        return result;
    }


    @Transactional
    @CachePut(value = "chair", key="#result.id")
    public ChairDTO saveChair(ChairDTO chairDTO)
    {
        log.info("Saving chair: {}", chairDTO);
        Chair chair = chairMapper.toEntity(chairDTO);
        ChairDTO saved = chairMapper.toDTO(chairRepository.save(chair));
        return saved;
    }

    @Transactional
    @CachePut(value = "chair", key="#result.id")
    public ChairDTO updateChair(Long id, ModifyChairRequest request)
    {
        Chair chair = chairRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chair not found"));
        chair.setOccupied(request.isOccupied());
        chair.setPositionX(request.getPositionX());
        chair.setPositionY(request.getPositionY());
        return chairMapper.toDTO(chair);
    }

    @Transactional
    @CacheEvict(value = "chair", key="#id")
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

    @Transactional
    @CachePut(value="chair", key="#id")
    public ChairDTO freeChairFromReservation(Long id)
    {
        Chair chair = chairRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chair not found"));
        chair.setReserved(false);
        return chairMapper.toDTO(chair);
    }

    @Transactional
    @CachePut(value="chair", key="#id")
    public ChairDTO reserveChair(Long id)
    {
        Chair chair = chairRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chair not found"));
        chair.setReserved(true);
        return chairMapper.toDTO(chair);
    }

    @Transactional
    @CachePut(value="chair", key="#id")
    public ChairDTO occupyChair(Long id)
    {
        log.info("Inside OCCUPYCHAIR, {}", id);
        Chair chair = chairRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chair not found"));
        if(chair.isOccupied())
        {
            throw new IllegalStateException("Resource already occupied!");
        }
        chair.setOccupied(true);
        chair.setOccupiedUntil(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
        return chairMapper.toDTO(chair);
    }

    @Transactional
    @CachePut(value="chair", key="#id")
    public ChairDTO freeChairFromOccupation(Long id)
    {
        Chair chair = getChairFromRepository(id);
        if(!chair.isOccupied())
        {
            throw new IllegalStateException("Chair is not occupied, nothing to free.");
        }
        chair.setOccupied(false);
        chair.setOccupiedUntil(null);
        return chairMapper.toDTO(chair);
    }

    public List<ChairDTO> getAllChairsList()
    {
        List<ChairDTO> chairDTOList = chairMapper.toDTOList(chairRepository.findAll());
        return chairDTOList;
    }

    private Chair getChairFromRepository(Long id)
    {
        Chair chair = chairRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chair not found"));
        return chair;
    }
}
