package com.Stefan.BibliotecaUnical.DTO.ChairDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChairDTO {

    private Long id;
    private boolean reserved;
    private Long tableId;
}
