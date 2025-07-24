package com.Stefan.BibliotecaUnical.DTO.LockerDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LockerDTO {
    private Long id;
    private Long version;
    private LocalDateTime occupiedUntil;
    private boolean isReserved;
    private boolean isOccupied;
    private Long reservationId;
    private String side;
}
