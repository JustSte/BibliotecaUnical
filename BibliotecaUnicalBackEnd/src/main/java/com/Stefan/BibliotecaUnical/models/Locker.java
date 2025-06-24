package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private boolean occupied;
    private LocalDateTime occupiedUntil;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean reserved;
    @Column(nullable = false)
    private int positionX;
    @Column(nullable = false)
    private int positionY;

}
