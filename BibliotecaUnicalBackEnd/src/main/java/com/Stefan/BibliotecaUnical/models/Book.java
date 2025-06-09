package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private Shelf shelf;

}
