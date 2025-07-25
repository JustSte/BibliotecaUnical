package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class OccupyResourceService {

    private final ApplicationEventPublisher eventPublisher;

    private final ChairService chairService;
    private final LockerService lockerService;

    @Transactional
    public void occupyResource(Long resourceId, String resourceType){
        if((resourceId == null || resourceId == 0) && resourceType.isBlank())
        {
            throw new ResourceNotFoundException("No such resource to occupy");
        }
        if(resourceType.equalsIgnoreCase("locker"))
        {
            lockerService.occupyLocker(resourceId);
        }
        else if (resourceType.equalsIgnoreCase("chair"))
        {
            chairService.occupyChair(resourceId);
        }
    }

/*    @Transactional
    public void freeResource(Long resourceId, String resourceType){
        if((resourceId == null || resourceId == 0) && resourceType.isBlank())
        {
            throw new ResourceNotFoundException("No such resource to free");
        }
        if(resourceType.equalsIgnoreCase("locker"))
        {
            lockerService.freeLocker(resourceId);
        }
        else if (resourceType.equalsIgnoreCase("chair"))
        {
            chairService.freeChair(resourceId);
        }
    }*/

    @Scheduled(cron = "0 0 0 * * ?")
    public void freeAllChairs()
    {
        log.info("Freeing all chairs.");

        List<ChairDTO> chairs = chairService.getAllChairsList();
        for(ChairDTO chair : chairs)
        {
            chair.setOccupied(false);
            chair.setOccupiedUntil(null);
            chair.setReserved(false);
            chair.setReservationId(null);
            chairService.saveChair(chair);
        }
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void freeAllLockers()
    {
        log.info("Freeing all lockers.");

        List<LockerDTO> lockers = lockerService.getAllLockersList();
        for(LockerDTO locker : lockers)
        {
            locker.setOccupied(false);
            locker.setOccupiedUntil(null);
            locker.setReserved(false);
            locker.setReservationId(null);
            lockerService.saveLocker(locker);
        }
    }

}
