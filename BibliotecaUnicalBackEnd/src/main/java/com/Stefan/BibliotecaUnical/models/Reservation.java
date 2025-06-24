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
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    private Long resourceId;

    private String userId;
    private String userMail;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime startTime;
    private LocalDateTime nextConfirmationTime;


    public enum ResourceType {
        CHAIR,
        LOCKER
    }

    public enum BookingStatus{
        PENDING_CONFIRMATION,
        ACTIVE,
        DONE,
        EXPIRED,
        CANCELLED
    }
}
