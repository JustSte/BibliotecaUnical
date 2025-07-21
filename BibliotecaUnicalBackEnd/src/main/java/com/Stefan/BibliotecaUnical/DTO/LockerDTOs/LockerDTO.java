package com.Stefan.BibliotecaUnical.DTO.LockerDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LockerDTO {
    private Long id;
    private Long version;
    private boolean isOccupied;
    private LocalDateTime occupiedUntil;
    private boolean isReserved;
    private Long reservationId;
    private String side;
}
