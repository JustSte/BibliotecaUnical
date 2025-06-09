package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTO;
import com.Stefan.BibliotecaUnical.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelf")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;

    @GetMapping
    public ResponseEntity<List<ShelfDTO>> getAllShelves()
    {
        return new ResponseEntity<>(shelfService.getAllShelves(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ShelfDTO> createShelf(@RequestBody ShelfDTO shelfDTO)
    {
        return new ResponseEntity<>(shelfService.saveShelf(shelfDTO), HttpStatus.CREATED);
    }
}
