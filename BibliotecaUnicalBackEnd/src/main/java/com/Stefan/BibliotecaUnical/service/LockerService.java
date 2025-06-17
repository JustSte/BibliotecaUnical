package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.LockerFlatDTO;
import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.mapper.LockerMapper;
import com.Stefan.BibliotecaUnical.models.Locker;
import com.Stefan.BibliotecaUnical.repository.LockerRepository;
import com.Stefan.BibliotecaUnical.request.ModifyLockerRequest;
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

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockerService {

    private final LockerMapper lockerMapper;
    private final LockerRepository lockerRepository;

    public Page<LockerDTO> getAllLockers(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Locker> lockerPage = lockerRepository.findAll(pageable);
        Page<LockerDTO> result = lockerPage.map(locker -> lockerMapper.toDTO(locker));
        return result;
    }

    @Cacheable(value = "locker", key="#id")
    public LockerDTO getLockerById(Long id)
    {
        LockerDTO lockerDTO = lockerMapper.toDTO(lockerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + id + " not found.")));
        return lockerDTO;
    }

    @CachePut(value = "locker", key = "#result.id")
    public LockerDTO saveLocker(LockerDTO lockerDTO)
    {
        Locker locker = lockerMapper.toEntity(lockerDTO);
        LockerDTO saved = lockerMapper.toDTO(lockerRepository.save(locker));
        log.info("Saved locker with id: {}", saved.getId());
        return saved;
    }

    public List<LockerDTO> getAllLockersList()
    {
        List<LockerDTO> lockerDTOList = lockerMapper.toDTOList(lockerRepository.findAll());
        return lockerDTOList;
    }

    @CachePut(value = "locker", key = "#result.id")
    public LockerDTO updateLocker(ModifyLockerRequest request)
    {
        LockerDTO lockerToModify = getLockerById(request.getLockerId());
        lockerToModify.setOccupied(request.isOccupied());
        lockerToModify.setPositionX(request.getPositionX());
        lockerToModify.setPositionY(request.getPositionY());
        LockerDTO modified = saveLocker(lockerToModify);
        return modified;

    }

    @CacheEvict(value = "locker", key = "#id")
    public void deleteLocker(Long id)
    {
        if(lockerRepository.existsById(id))
        {
            lockerRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Locker with id: " + id + " not found.");
        }
    }

}
