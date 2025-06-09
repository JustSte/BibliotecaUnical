package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.CreateShelfDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.request.AddBookRequest;
import com.Stefan.BibliotecaUnical.service.ShelfService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CreateShelfDTO> createShelf(@RequestBody CreateShelfDTO createShelfDTO)
    {
        return new ResponseEntity<>(shelfService.createShelf(createShelfDTO), HttpStatus.CREATED);
    }

    @PostMapping("/addBooks")
    public ResponseEntity<ShelfDTO> addBooksToShelf(@Valid @RequestBody AddBookRequest request)
    {
        return new ResponseEntity<>(shelfService.addBooksToShelf(request.getShelfId(), request.getBookList()), HttpStatus.OK);
    }

}
