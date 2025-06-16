package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import lombok.Data;

@Data
public class LockerFlatDTO {

    private Long id;

    private boolean occupied;
    private String location;
}
