package com.Stefan.BibliotecaUnical.DTO.LockerDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LockerDTO {
    private Long id;
    private boolean occupied;
    private LocalDateTime occupiedUntil;
    private int positionX;
    private int positionY;
}
