package com.Stefan.BibliotecaUnical.request;

import com.Stefan.BibliotecaUnical.models.LibraryTable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModifyChairRequest {
    private boolean isReserved;
    private LibraryTable libraryTable;
    private long reservationId;
    private LocalDateTime occupiedUntil;
}
