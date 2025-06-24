package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.event.MapLayoutUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public void occupyChair(Long chairId)
    {
        log.info("Occupying chair {}", chairId);
        ChairDTO chairDTO = chairService.getChairById(chairId);

        if (chairDTO.isOccupied())
        {
            throw new IllegalStateException("Chair already occupied.");
        }

        chairDTO.setOccupied(true);
        chairDTO.setOccupiedUntil(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
        chairService.saveChair(chairDTO);

        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
    }

    @Transactional
    public void occupyLocker(Long lockerId)
    {
        log.info("Occupying locker {}", lockerId);
        LockerDTO lockerDTO = lockerService.getLockerById(lockerId);

        if(lockerDTO.isOccupied())
        {
            throw new IllegalStateException("Locker is already occupied.");
        }

        lockerDTO.setOccupied(true);
        lockerDTO.setOccupiedUntil(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES));
        lockerService.saveLocker(lockerDTO);

        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
    }

    @Transactional
    public void freeChair(Long chairId)
    {
        log.info("Freeing chair with id: {}", chairId);

        ChairDTO chairDTO = chairService.getChairById(chairId);
        if(!chairDTO.isOccupied())
        {
            throw new IllegalStateException("Chair is not occupied, nothing to free.");
        }

        chairDTO.setOccupied(false);
        chairDTO.setOccupiedUntil(null);
        chairService.saveChair(chairDTO);

        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
    }

    @Transactional
    public void freeLocker(Long lockerId)
    {
        log.info("Freeing locker with id: {}", lockerId);

        LockerDTO lockerDTO = lockerService.getLockerById(lockerId);
        if(!lockerDTO.isOccupied())
        {
            throw new IllegalStateException("Locker is not occupied, nothing to free.");
        }

        lockerDTO.setOccupied(false);
        lockerDTO.setOccupiedUntil(null);
        lockerService.saveLocker(lockerDTO);

        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void freeAllChairs()
    {
        log.info("Freeing all chairs.");

        List<ChairDTO> chairs = chairService.getAllChairsList();
        for(ChairDTO chair : chairs)
        {
            chair.setOccupied(false);
            chair.setOccupiedUntil(null);
            chairService.saveChair(chair);
        }
        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
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
            lockerService.saveLocker(locker);
        }
        eventPublisher.publishEvent(new MapLayoutUpdatedEvent(this));
    }

}
