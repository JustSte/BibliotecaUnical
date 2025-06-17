package com.Stefan.BibliotecaUnical.DTO;

import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.LibraryTableFlatDTO;
import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import lombok.Data;

import java.util.List;

@Data
public class MapLayoutDTO {
    private List<LibraryTableFlatDTO> tables;
    private List<LockerDTO> lockers;
}
