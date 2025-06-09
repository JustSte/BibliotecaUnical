package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Shelf {

    @Id
    private Long id;

    @Max(100)
    private int size;

    private String location;

    @OneToMany
    @Size(max=100)
    private Book[] books;
}
