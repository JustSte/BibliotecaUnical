package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.ReservationDTOs.ReservationDTO;
import com.Stefan.BibliotecaUnical.models.Reservation;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation toEntity(ReservationDTO reservationDTO);
    ReservationDTO toDTO(Reservation reservation);

    List<Reservation> toEntityList(List<ReservationDTO> reservationDTOList);
    List<ReservationDTO> toDTOList(List<Reservation> reservationList);
}
