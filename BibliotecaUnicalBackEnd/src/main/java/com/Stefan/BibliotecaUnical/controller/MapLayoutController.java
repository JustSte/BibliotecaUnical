package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.MapLayoutDTO;
import com.Stefan.BibliotecaUnical.service.MapLayoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mapLayout")
@Tag(name = "MapLayout", description = "Get a map of the library's layout")
public class MapLayoutController {

    private final MapLayoutService mapLayoutService;

    @PreAuthorize("hasAnyRole('USER' ,'STAFF', 'ADMIN')")
    @GetMapping
    @Operation(summary = "Retrive a layout of the library")
    public ResponseEntity<MapLayoutDTO> getMapLayout()
    {
        MapLayoutDTO mapLayoutDTO = mapLayoutService.getMapLayout();
        return new ResponseEntity<>(mapLayoutDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping
    @Operation(summary = "Refresh map if stuck")
    public ResponseEntity<String> refreshMapLayout()
    {
        mapLayoutService.refreshMapLayout();
        return new ResponseEntity<>(("Layout refreshed."), HttpStatus.OK);
    }
}
