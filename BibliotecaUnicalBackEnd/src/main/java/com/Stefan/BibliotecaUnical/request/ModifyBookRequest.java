package com.Stefan.BibliotecaUnical.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyBookRequest {

    @NotNull
    private Long bookId;
    
    private String title;

    private Long shelfId;
}
