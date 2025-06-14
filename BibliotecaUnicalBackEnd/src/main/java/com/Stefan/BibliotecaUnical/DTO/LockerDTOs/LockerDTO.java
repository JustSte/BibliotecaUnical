package com.Stefan.BibliotecaUnical.DTO.LockerDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LockerDTO {
    private Long id;
    private boolean occupied;
    private String location;
}
