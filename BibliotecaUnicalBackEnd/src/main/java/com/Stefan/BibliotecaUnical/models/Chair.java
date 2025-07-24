package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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

    @Version
    @Column(nullable = false)
    private Long version;

    private LocalDateTime occupiedUntil;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isReserved;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isOccupied;
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "libraryTable_id")
    private LibraryTable libraryTable;
}
