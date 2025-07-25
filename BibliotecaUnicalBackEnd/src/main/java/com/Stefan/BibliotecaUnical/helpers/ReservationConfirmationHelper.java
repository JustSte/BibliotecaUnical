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

import java.time.LocalDateTime;
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
                "Confirm your reservation: %s/confirm/%d",
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
            resourceService.occupyResource(reservation.getResourceId(), reservation.getResourceType());
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
            expireConfirmationRequest(reservation.getUserId());
        }
    }

    public void expireConfirmationRequest(String userId)
    {
        precisionScheduler.scheduleWithDelay(() -> {
            LocalDateTime timeNow = LocalDateTime.now().minusMinutes(15);
            ReservationDTO reservationDTO = reservationService.getReservationToExpireForUser(userId, timeNow);
            reservationDTO.setStatus("EXPIRED");
            userService.incrementMissedConfirmations(reservationDTO.getUserId());
            reservationService.freeResource(reservationDTO.getResourceType(), reservationDTO.getResourceId());
            reservationService.saveReservation(reservationDTO);
            userService.removeReserveResource(reservationDTO.getResourceType(), reservationDTO.getUserId());
            sendMissedReservation(reservationDTO);
        });
    }
}
