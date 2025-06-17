package com.Stefan.BibliotecaUnical.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyBookRequest {
    
    private String title;

    private Long shelfId;
}
