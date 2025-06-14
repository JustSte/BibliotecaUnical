package com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LibraryTableDTO {

    private Long id;
    private String name;
    private String location;
    List<ChairSummaryDTO> chairs;
}
