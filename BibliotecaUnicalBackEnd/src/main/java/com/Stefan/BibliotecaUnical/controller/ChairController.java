package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.service.ChairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chairs")
@RequiredArgsConstructor
@Tag(name = "Chairs", description = "Manage chairs of table")
public class ChairController {

    private final ChairService chairService;

    @PreAuthorize("hasAnyRole('USER' , 'STAFF' , 'ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Retrive a chair by it's id")
    public ResponseEntity<ChairDTO> getChairById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(chairService.getChairById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF' , 'ADMIN')")
    @GetMapping
    @Operation(summary = "Retrive all chairs")
    public ResponseEntity<Page<ChairDTO>> getAllChairs(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "8") int size)
    {
        return new ResponseEntity<>(chairService.getAllChairs(page, size), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF' , 'ADMIN')")
    @PostMapping
    @Operation(summary = "Create a chair (don't use, for test only, use addChairsOfTable)")
    public ResponseEntity<ChairDTO> saveChair(@RequestBody ChairDTO chairDTO)
    {
        return new ResponseEntity<>(chairService.saveChair(chairDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER' , 'STAFF' , 'ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a chair")
    public ResponseEntity<String> deleteChair(@PathVariable Long id)
    {
        chairService.deleteChair(id);
        return new ResponseEntity<>(("Chair with id: " + id + " deleted successfully."), HttpStatus.OK);
    }
}
