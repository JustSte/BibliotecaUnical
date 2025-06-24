package com.Stefan.BibliotecaUnical.DTO.ReservationDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationDTO {

    private Long id;

    private String resourceType;
    private Long resourceId;

    private String userId;
    private String userMail;

    private String status;

    private LocalDateTime startTime;
    private LocalDateTime nextConfirmationTime;
}
