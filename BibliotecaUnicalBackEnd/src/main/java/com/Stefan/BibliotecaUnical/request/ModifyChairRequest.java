package com.Stefan.BibliotecaUnical.request;

import lombok.Data;

@Data
public class ModifyChairRequest {
    private boolean occupied;
    private int positionX;
    private int positionY;
}
