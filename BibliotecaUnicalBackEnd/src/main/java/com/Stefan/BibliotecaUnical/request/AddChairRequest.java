package com.Stefan.BibliotecaUnical.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AddChairRequest {

        @NotNull(message= "Table ID must not be null")
        private Long libraryTableId;

        @NotNull(message= "Chair list must not be null")
        private List<Long> chairList;
}
