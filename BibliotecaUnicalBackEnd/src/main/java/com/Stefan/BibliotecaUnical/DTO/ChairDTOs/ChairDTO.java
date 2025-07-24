package com.Stefan.BibliotecaUnical.DTO.ChairDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChairDTO {

    private Long id;
    private Long version;
    private LocalDateTime occupiedUntil;
    private boolean isReserved;
    private boolean isOccupied;
    private Long reservationId;
    private Long libraryTableId;
}
