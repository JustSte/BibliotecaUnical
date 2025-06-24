package com.Stefan.BibliotecaUnical.request;

import lombok.Data;

@Data
public class ReservationRequest {
    private String resourceType;
    private Long resourceId;

    private String userId;

}
