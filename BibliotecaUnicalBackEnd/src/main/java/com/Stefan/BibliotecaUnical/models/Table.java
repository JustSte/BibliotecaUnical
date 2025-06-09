package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Table {

    @Id
    private Long id;

    @NotBlank
    private String name;

    @Size(min=1, max = 8)
    private List<Boolean> seatAvailable;

}
