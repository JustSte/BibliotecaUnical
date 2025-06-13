package com.Stefan.BibliotecaUnical.DTO.ChairDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChairDTO {

    private Long id;
    private boolean reserved;
    private Long tableId;
}
