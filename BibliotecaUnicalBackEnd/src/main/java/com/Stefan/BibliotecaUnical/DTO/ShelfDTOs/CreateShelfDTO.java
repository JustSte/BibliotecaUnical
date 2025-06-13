package com.Stefan.BibliotecaUnical.DTO.ShelfDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CreateShelfDTO {

    private Long id;
    private int size;
    private String location;
}
