package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.ReservationDTOs.ReservationDTO;
import com.Stefan.BibliotecaUnical.mapper.ReservationMapper;
import com.Stefan.BibliotecaUnical.models.Reservation;
import com.Stefan.BibliotecaUnical.repository.ReservationRepository;
import com.Stefan.BibliotecaUnical.request.ReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final ChairService chairService;
    private final LockerService lockerService;
    private final KeycloakUserService userService;

    @Transactional
    public ReservationDTO createReservation(ReservationRequest request, String userMail)
    {
        log.info("\nRequest : {}", request);
        if (!checkAvailability(request))
        {
            log.warn(String.valueOf(checkAvailability(request)), " checkAvailability");
            return null;
            //throw new IllegalStateException("Resource not available to reserve.");
        }
        ReservationDTO reservation = new ReservationDTO();
        reservation.setStatus("PENDING_CONFIRMATION");
        reservation.setStartTime(LocalDateTime.now());
        reservation.setNextConfirmationTime(LocalDateTime.now().plusHours(2));
        reservation.setResourceType(request.getResourceType().toUpperCase());
        reservation.setResourceId(request.getResourceId());
        reservation.setUserId(request.getUserId());
        reservation.setUserMail(userMail);

        ReservationDTO saved = saveReservation(reservation);
        log.warn("Before reserve resource ub reservationService");
        reserveResource(saved.getResourceType(), saved.getResourceId(), saved.getId());
        userService.setResourceReserve(request.getResourceType(), request.getUserId(), request.getResourceId());
        return saved;
    }

    @Transactional
    public void cancelReservation(Long id, String userId)
    {
        ReservationDTO reservation = getReservationById(id);
        if (reservation.getUserId().equals(userId))
        {
            reservation.setStatus("CANCELLED");
            freeResource(reservation.getResourceType(), reservation.getResourceId());
            saveReservation(reservation);
            userService.removeReserveResource(reservation.getResourceType(), reservation.getUserId());
        }
    }

    @Transactional
    public ReservationDTO saveReservation(ReservationDTO reservationDTO)
    {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        ReservationDTO saved = reservationMapper.toDTO(reservationRepository.save(reservation));
        return saved;
    }

    @Transactional
    private boolean checkAvailability(ReservationRequest reservation)
    {
        log.info("Reservation: {}", reservation);
        String type = reservation.getResourceType();
        boolean available = false;
        if (type == null)
        {
            throw new ResourceNotFoundException("No resource selected.");
        }
        else if (type.equalsIgnoreCase("CHAIR"))
        {
            if (chairService.getChairById(reservation.getResourceId()).isReserved() && userService.getSeatReserved(reservation.getUserId()) != 0)
            {
                throw new IllegalStateException("Chair not available, check again later or choose another chair.");
            }
            else available = true;
        }
        else if (type.equalsIgnoreCase("LOCKER"))
        {
            if (lockerService.getLockerById(reservation.getResourceId()).isReserved() && userService.getLockerReserved(reservation.getUserId()) != 0)
            {
                throw new IllegalStateException("Locker not available, check again later or choose another locker.");
            }
            else available = true;
        }
        return available;
    }

    public ReservationDTO getReservationById(Long id)
    {
        ReservationDTO reservation = reservationMapper.toDTO(reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No reservation found.")));
        return reservation;
    }

    public ReservationDTO getReservationByUserId(String id)
    {
        ReservationDTO reservation = reservationMapper.toDTO(reservationRepository.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException("No reservation found.")));
        return reservation;
    }

    public List<ReservationDTO> getActiveAndNeedingReconfirmation()
    {
        List<ReservationDTO> reservationDTOList = reservationMapper.toDTOList(reservationRepository.findActiveReservationsNeedingConfirmation(LocalDateTime.now()));
        return reservationDTOList;
    }

    public ReservationDTO getActiveReservationForUser(String userId, String resourceType){
        if(userId.isBlank() || resourceType.isBlank())
        {
            throw new ResourceNotFoundException("No such resource found!");
        }
        Reservation.ResourceType resource;
        if(resourceType.equalsIgnoreCase("locker"))
        {
            resource = Reservation.ResourceType.LOCKER;
        }
        else
        {
            resource = Reservation.ResourceType.CHAIR;
        }
        ReservationDTO reservationDTO = reservationMapper.toDTO(reservationRepository.findActiveReservationForUser(userId, resource));
        return reservationDTO;
    }

    public ReservationDTO getReservationToExpireForUser(String userId, LocalDateTime time)
    {
        ReservationDTO reservationDTO = reservationMapper.toDTO(reservationRepository.findReservationToExpire(time, userId));
        return reservationDTO;
    }

    public void reserveResource(String resourceType, Long resourceId, Long reservationId)
    {
        if (resourceType == null || resourceId == null)
        {
            throw new ResourceNotFoundException("No such resource.");
        }
        if (resourceType.equals("CHAIR"))
        {
            chairService.reserveChair(resourceId);
        }
        if (resourceType.equals("LOCKER"))
        {
            lockerService.reserveLocker(resourceId, reservationId);
        }
    }

    public void freeResource(String resourceType, Long resourceId)
    {
        if (resourceType == null || resourceId == null)
        {
            throw new ResourceNotFoundException("No such resource.");
        }
        if (resourceType.equals("CHAIR"))
        {
            chairService.freeChairFromReservation(resourceId);
        }
        if (resourceType.equals("LOCKER"))
        {
            lockerService.freeLockerFromReservation(resourceId);
        }
    }
}
