package com.Stefan.BibliotecaUnical.service;

import com.Stefan.BibliotecaUnical.DTO.LibraryTableDTOs.LibraryTableDTO;
import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.DTO.MapLayoutDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapLayoutService {

    private final LibraryTableService libraryTableService;
    private final LockerService lockerService;

    @Cacheable(value = "mapLayout", key = "'layout'", sync = true)
    public MapLayoutDTO getMapLayout()
    {
        return generateMapLayout();
    }

    @CachePut(value = "mapLayout", key = "'layout'")
    public MapLayoutDTO generateMapLayout()
    {
        MapLayoutDTO mapLayout = new MapLayoutDTO();
        List<LibraryTableDTO> tables = libraryTableService.getAllTablesList();
        mapLayout.setTables(tables);
        List<LockerDTO> lockers = lockerService.getAllLockersList();
        mapLayout.setLockers(lockers);
        return mapLayout;
    }

    @CacheEvict(value = "mapLayout", key ="'layout'")
    public void refreshMapLayout()
    {
        getMapLayout();
    }

}
