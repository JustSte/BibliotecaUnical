package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LockerFlatDTO {

    private Long id;

    private boolean occupied;
    private LocalDateTime occupiedUntil;
    private int positionX;
    private int positionY;
}
