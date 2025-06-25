package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ReservationDTOs.ReservationDTO;
import com.Stefan.BibliotecaUnical.helpers.ReservationConfirmationHelper;
import com.Stefan.BibliotecaUnical.request.ReservationRequest;
import com.Stefan.BibliotecaUnical.service.KeycloakUserService;
import com.Stefan.BibliotecaUnical.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final KeycloakUserService userService;
    private final ReservationConfirmationHelper reservationConfirmationHelper;

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request, @AuthenticationPrincipal Jwt jwt)
    {
        String userId = jwt.getClaimAsString("sub");
        String userMail = jwt.getClaimAsString("email");
        request.setUserId(userId);
        try
        {
            ReservationDTO reservationDTO = reservationService.createReservation(request, userMail);
            reservationConfirmationHelper.sendConfirmation(reservationDTO);
            reservationConfirmationHelper.expireConfirmationRequest(reservationDTO, LocalDateTime.now());
            return new ResponseEntity<>(reservationDTO, HttpStatus.CREATED);
        }
        catch(ObjectOptimisticLockingFailureException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Someone already reserved this resource. Refresh the page.");
        }
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt)
    {
        String userId = jwt.getClaimAsString("sub");
        reservationConfirmationHelper.confirmReservation(id, userId);
        return new ResponseEntity<>(("Reservation confirmed successfully."), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt)
    {
        String userId = jwt.getClaimAsString("sub");
        reservationService.cancelReservation(id, userId);
        return new ResponseEntity<>(("Reservation cancelled successfully"), HttpStatus.NO_CONTENT);
    }



}
