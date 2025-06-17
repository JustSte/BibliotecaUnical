package com.Stefan.BibliotecaUnical.DTO;

import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import lombok.Data;

import java.util.List;

@Data
public class MapLayoutDTO {
    private List<LibraryTableDTO> tables;
    private List<LockerDTO> lockers;
}
