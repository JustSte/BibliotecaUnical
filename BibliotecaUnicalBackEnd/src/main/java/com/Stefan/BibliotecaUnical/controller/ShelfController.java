package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.ShelfFlatDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.CreateShelfDTO;
import com.Stefan.BibliotecaUnical.DTO.ShelfDTOs.ShelfDTO;
import com.Stefan.BibliotecaUnical.helpers.ShelfBookHelper;
import com.Stefan.BibliotecaUnical.request.AddBookRequest;
import com.Stefan.BibliotecaUnical.service.ShelfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shelf")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;
    private final ShelfBookHelper shelfBookHelper;

    @GetMapping
    public ResponseEntity<Page<ShelfDTO>> getAllShelves(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "15") int size)
    {
        return new ResponseEntity<>(shelfService.getAllShelves(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDTO> getShelfById(@PathVariable Long id)
    {
        return new ResponseEntity<>(shelfService.getShelfById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CreateShelfDTO> createShelf(@RequestBody CreateShelfDTO createShelfDTO)
    {
        return new ResponseEntity<>(shelfService.createShelf(createShelfDTO), HttpStatus.CREATED);
    }

    @PostMapping("/addBooks")
    public ResponseEntity<ShelfDTO> addBooksToShelf(@Valid @RequestBody AddBookRequest request)
    {
        return new ResponseEntity<>(shelfBookHelper.addBooksToShelf(request.getShelfId(), request.getBookList()), HttpStatus.OK);
    }

    @PostMapping("/deleteBooks")
    public ResponseEntity<ShelfDTO> deleteBooksFromShelf(@Valid @RequestBody AddBookRequest request)
    {
        return new ResponseEntity<>(shelfBookHelper.deleteBooksFromShelf(request.getShelfId(), request.getBookList()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShelf(@PathVariable Long id)
    {
        shelfService.deleteShelf(id);
        return new ResponseEntity<>(("Shelf with id: " + id + " successfully deleted."), HttpStatus.OK);
    }
}
