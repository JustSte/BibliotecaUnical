package com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairSummaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LibraryTableDTO {

    private Long id;
    private String name;
    private String location;
    List<ChairSummaryDTO> chairs;
}
