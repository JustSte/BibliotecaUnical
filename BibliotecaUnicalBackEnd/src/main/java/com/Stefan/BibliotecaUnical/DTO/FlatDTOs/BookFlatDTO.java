package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import lombok.Data;

@Data
public class BookFlatDTO {
    private Long id;
    private String title;
    private Long shelfId;
}
