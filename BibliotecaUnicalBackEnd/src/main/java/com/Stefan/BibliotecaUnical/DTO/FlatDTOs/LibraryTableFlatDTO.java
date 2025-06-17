package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import lombok.Data;

import java.util.List;

@Data
public class LibraryTableFlatDTO {

    private Long id;
    private String name;
    private int positionX;
    private int positionY;
    private List<ChairFlatDTO> chairs;
}
