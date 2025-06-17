package com.Stefan.BibliotecaUnical.DTO.ShelfDTOs;

import com.Stefan.BibliotecaUnical.DTO.BookDTOs.BookDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShelfDTO {

    private Long id;
    private int size;
    private String location;
    private List<BookDTO> books;
}
