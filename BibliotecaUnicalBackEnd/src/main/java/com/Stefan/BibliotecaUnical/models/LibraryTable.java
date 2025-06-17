package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class LibraryTable {

    @Id
    private Long id;

    private String name;
    @Column(nullable = false)
    private int positionX;
    @Column(nullable = false)
    private int positionY;

    @OneToMany(mappedBy = "libraryTable")
    private List<Chair> chairs = new ArrayList<>();

}
