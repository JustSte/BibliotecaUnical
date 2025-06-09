package com.Stefan.BibliotecaUnical.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TableDTO {

    private Long id;
    private String name;
    private List<Boolean> seatAvailable;

}
