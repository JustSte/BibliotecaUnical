package com.Stefan.BibliotecaUnical.DTO.ChairDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChairDTO {

    private Long id;
    private boolean occupied;
    private LocalDateTime occupiedUntil;
    private boolean reserved;
    private int positionX;
    private int positionY;
    private Long libraryTableId;
}
