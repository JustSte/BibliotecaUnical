package com.Stefan.BibliotecaUnical.DTO.BookDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Long shelfID;
    private String shelfLocation;
}
