package com.Stefan.BibliotecaUnical.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyTableRequest {

    private String name;
    private int positionX;
    private int positionY;
}
