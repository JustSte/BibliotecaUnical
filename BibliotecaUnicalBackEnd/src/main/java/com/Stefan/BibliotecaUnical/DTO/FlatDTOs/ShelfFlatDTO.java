package com.Stefan.BibliotecaUnical.DTO.FlatDTOs;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookSummaryDTO;
import lombok.Data;

import java.util.List;

@Data
public class ShelfFlatDTO {
    private Long id;
    private String location;
    //DO i really have to send the books too?
    private List<BookSummaryDTO> books;
}
