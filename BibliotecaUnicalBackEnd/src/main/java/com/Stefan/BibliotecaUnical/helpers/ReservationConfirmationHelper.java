package com.Stefan.BibliotecaUnical.helpers;

import com.Stefan.BibliotecaUnical.DTO.ReservationDTOs.ReservationDTO;
import com.Stefan.BibliotecaUnical.mapper.ReservationMapper;
import com.Stefan.BibliotecaUnical.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
@EnableScheduling
public class ReservationConfirmationHelper {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final OccupyResourceService resourceService;

    private final PrecisionScheduler precisionScheduler;

    private final KeycloakUserService userService;
    private final EmailService emailService;

    @Async
    public void sendConfirmation(ReservationDTO reservation)
    {
        String mailText = String.format(
                "Confirm your reservation: %s/%d/confirm",
                "localhost:8080/api/reservation",
                reservation.getId()
        );
        emailService.sendSimpleMail(reservation.getUserMail(), "Confirm your reservation", mailText);
    }

    public void confirmReservation(Long resourceId, String userId)
    {
        ReservationDTO reservation = reservationService.getReservationById(resourceId);
        if(!reservation.getUserId().equals(userId))
        {
            throw new IllegalStateException("Reservation is made by someone else, you cannot confirm for other people.");
        }
        if(reservation.getStatus().equals("PENDING_CONFIRMATION"))
        {
            reservation.setStatus("ACTIVE");
            reservation.setNextConfirmationTime(LocalDateTime.now().plusHours(2));
            reservationService.saveReservation(reservation);
            if(reservation.getResourceType().equals("CHAIR"))
            {
                resourceService.occupyChair(reservation.getResourceId());
            }
            else if (reservation.getResourceType().equals("LOCKER"))
            {
                resourceService.occupyLocker(reservation.getResourceId());
            }
        }
    }

    public void sendMissedReservation(ReservationDTO reservation)
    {
        String mailText = String.format(
                "The reservation made for %s with id %d has been cancelled.\n" +
                        "Your counter for missed confirmations has been increased to: %d",
                reservation.getResourceType().toLowerCase(),
                reservation.getResourceId(),
                userService.getMissedConfirmationsCount(reservation.getUserId())
        );
        emailService.sendSimpleMail(reservation.getUserMail(), "Missed reservation", mailText);
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void checkForReconfirmation()
    {
        log.info("Executing checkForReconfirmation at: {}", LocalDateTime.now());
        List<ReservationDTO> reservationDTOList = reservationService.getActiveAndNeedingReconfirmation();
        for(ReservationDTO reservation : reservationDTOList)
        {
            reservation.setStatus("PENDING_CONFIRMATION");
            sendConfirmation(reservation);
            expireConfirmationRequest();
        }
    }

    public void expireConfirmationRequest()
    {
        precisionScheduler.scheduleWithDelay(() -> {
            LocalDateTime timeNow = LocalDateTime.now().minusMinutes(15);
            ReservationDTO reservationDTO = reservationService.getReservationToExpire(timeNow);
            reservationDTO.setStatus("EXPIRED");
            userService.incrementMissedConfirmations(reservationDTO.getUserId());
            reservationService.saveReservation(reservationDTO);
            reservationService.freeResource(reservationDTO.getResourceType(), reservationDTO.getResourceId());
            sendMissedReservation(reservationDTO);
        });
    }
}
