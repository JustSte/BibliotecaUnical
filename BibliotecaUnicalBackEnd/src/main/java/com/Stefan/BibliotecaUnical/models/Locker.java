package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "locker", indexes = {
        @Index(name = "idx_locker_side", columnList = "side")
})
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;
    @Column(nullable = false)
    private boolean isOccupied;
    private LocalDateTime occupiedUntil;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isReserved;
    private Long reservationId;
    private String side;

}
