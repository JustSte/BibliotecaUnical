package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "libraryTable")
    @OrderBy("id ASC")
    private List<Chair> chairs = new ArrayList<>();

}
