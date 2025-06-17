package com.Stefan.BibliotecaUnical.mapper;

import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.LockerFlatDTO;
import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.models.Locker;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface LockerMapper {

    Locker toEntity(LockerDTO lockerDTO);
    LockerDTO toDTO(Locker locker);
    LockerDTO toDTOFromFlat(LockerFlatDTO lockerFlatDTO);

    List<Locker> toEntityList(List<LockerDTO> lockerDTOList);
    List<LockerDTO> toDTOList(List<Locker> lockerList);

    Locker toEntityFromFlat(LockerFlatDTO lockerFlatDTO);
    LockerFlatDTO toFlatFromEntity(Locker locker);
    List<LockerFlatDTO> toFlatListFromEntity(List<Locker> lockerList);


}
