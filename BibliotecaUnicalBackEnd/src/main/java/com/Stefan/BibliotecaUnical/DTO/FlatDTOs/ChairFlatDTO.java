package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import lombok.Data;

@Data
public class ChairFlatDTO {
    private Long id;
    private boolean reserved;
    private Long libraryTableId;
}
