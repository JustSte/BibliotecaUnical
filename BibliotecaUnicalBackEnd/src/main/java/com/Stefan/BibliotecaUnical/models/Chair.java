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
public class Chair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chairSeqGen")
    @SequenceGenerator(name = "chairSeqGen", sequenceName = "chair_sequence", allocationSize = 8)
    private Long id;

    private boolean reserved;

    @ManyToOne
    @JoinColumn(name = "libraryTable_id")
    private LibraryTable libraryTable;
}
