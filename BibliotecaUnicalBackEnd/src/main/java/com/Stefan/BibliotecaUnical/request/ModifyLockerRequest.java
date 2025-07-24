package com.Stefan.BibliotecaUnical.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyLockerRequest {
    @NotNull
    private Long lockerId;

    private boolean isReserved;

}
