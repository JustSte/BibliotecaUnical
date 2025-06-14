package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.mapper.LockerMapper;
import com.Stefan.BibliotecaUnical.models.Locker;
import com.Stefan.BibliotecaUnical.repository.LockerRepository;
import com.Stefan.BibliotecaUnical.request.ModifyLockerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockerService {

    private final LockerMapper lockerMapper;
    private final LockerRepository lockerRepository;

    public List<LockerDTO> getAllLockers()
    {
        List<LockerDTO> lockerDTOList = lockerMapper.toDTOList(lockerRepository.findAll());
        return lockerDTOList;
    }

    public LockerDTO getLockerById(Long id)
    {
        LockerDTO lockerDTO = lockerMapper.toDTO(lockerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Locker with id : " + id + " not found.")));
        return lockerDTO;
    }

    public LockerDTO saveLocker(LockerDTO lockerDTO)
    {
        Locker locker = lockerMapper.toEntity(lockerDTO);
        LockerDTO saved = lockerMapper.toDTO(lockerRepository.save(locker));
        log.info("Saved locker with id: {}", saved.getId());
        return saved;
    }

    public LockerDTO updateLocker(ModifyLockerRequest request)
    {
        LockerDTO lockerToModify = getLockerById(request.getLockerId());
        lockerToModify.setOccupied(request.isOccupied());
        lockerToModify.setLocation(request.getLocation());
        LockerDTO modified = saveLocker(lockerToModify);
        return modified;

    }

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
