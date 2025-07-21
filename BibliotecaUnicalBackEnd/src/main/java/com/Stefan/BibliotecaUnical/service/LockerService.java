package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.mapper.LockerMapper;
import com.Stefan.BibliotecaUnical.models.Locker;
import com.Stefan.BibliotecaUnical.repository.LockerRepository;
import com.Stefan.BibliotecaUnical.request.ModifyLockerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
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
        LockerDTO lockerDTO = lockerMapper.toDTO(lockerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + id + " not found.")));
        return lockerDTO;
    }

    @Transactional
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

    @Transactional
    public List<LockerDTO> lockerDTOListBySide(String side)
    {
        List<LockerDTO> lockerDTOList = lockerMapper.toDTOList(lockerRepository.findBySideIgnoreCaseOrderById(side));

        return lockerDTOList;
    }

    @Transactional
    @CachePut(value = "locker", key = "#request.lockerId")
    public LockerDTO updateLocker(ModifyLockerRequest request)
    {
        Locker locker = lockerRepository.findById(request.getLockerId())
                .orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + request.getLockerId() + " not found."));

        locker.setOccupied(request.isOccupied());
        return lockerMapper.toDTO(locker);

    }

    @Transactional
    @CachePut(value = "locker", key = "#id")
    public LockerDTO reserveLocker(Long id, Long reservationId)
    {
        Locker lockerToSave = lockerMapper.toEntity(getLockerById(id));
        lockerToSave.setReserved(true);
        lockerToSave.setOccupiedUntil(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
        lockerToSave.setReservationId(reservationId);
        Locker savedLocker = lockerRepository.save(lockerToSave);
        return lockerMapper.toDTO(savedLocker);
    }

    @Transactional
    @CachePut(value = "locker", key = "#id")
    public LockerDTO freeLockerFromReservation(Long id)
    {
        Locker locker = lockerMapper.toEntity(getLockerById(id));
        locker.setReserved(false);
        locker.setOccupiedUntil(null);
        locker.setReservationId(null);
        Locker savedLocker = lockerRepository.save(locker);
        return lockerMapper.toDTO(savedLocker);
    }

    @Transactional
    @CachePut(value = "locker", key = "#id")
    public LockerDTO occupyLocker(Long id)
    {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + id + " not found."));
        if(locker.isOccupied())
        {
            throw new IllegalStateException("Resource already occupied!");
        }
        locker.setOccupied(true);
        locker.setOccupiedUntil(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
        return lockerMapper.toDTO(locker);
    }

    @Transactional
    @CachePut(value = "locker", key = "#id")
    public LockerDTO freeLockerFromOccupation(Long id)
    {
        Locker locker = getLockerFromRepository(id);
        if(!locker.isOccupied())
        {
            throw new IllegalStateException("Locker is not occupied, nothing to free.");
        }
        locker.setOccupied(false);
        locker.setOccupiedUntil(null);
        return lockerMapper.toDTO(locker);
    }

    @Transactional
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

    private Locker getLockerFromRepository(Long id)
    {
        Locker locker = lockerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + id + " not found."));
        return locker;
    }

}
