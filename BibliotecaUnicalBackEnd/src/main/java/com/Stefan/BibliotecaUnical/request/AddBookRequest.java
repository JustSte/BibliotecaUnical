package com.Stefan.BibliotecaUnical.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddBookRequest {

    @NotNull(message= "Shelf ID must not be null")
    private Long shelfId;

    @NotNull(message= "Book list must not be null")
    private List<Long> bookList;
}
