package com.Stefan.BibliotecaUnical.controller;


import com.Stefan.BibliotecaUnical.service.OccupyResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Occupacy", description = "Manage chair and locker reservation")
public class OccupationController {

    private final OccupyResourceService occupyResourceService;

    @PreAuthorize("hasAnyRole('USER' , 'STAFF', 'ADMIN')")
    @PostMapping("/resource/{resourceType}/id/{id}")
    @Operation(summary = "Reserve chair")
    public ResponseEntity<Void> occupyResouce(@PathVariable Long id, @PathVariable String resourceType)
    {
        if(id == null || resourceType.isBlank())
        {
            return ResponseEntity.notFound().build();
        }
        occupyResourceService.occupyResource(id, resourceType);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/freeAllLockers")
    @Operation(summary = "Make all lockers free")
    public ResponseEntity<Void> freeAllLockers()
    {
        occupyResourceService.freeAllLockers();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/freeAllChairs")
    @Operation(summary = "Make all chairs free")
    public ResponseEntity<Void> freeAllChairs()
    {
        occupyResourceService.freeAllChairs();
        return ResponseEntity.ok().build();
    }
}
