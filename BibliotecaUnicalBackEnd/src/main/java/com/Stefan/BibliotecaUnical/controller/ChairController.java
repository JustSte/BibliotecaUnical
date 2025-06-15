package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.DTO.FlatDTOs.ChairFlatDTO;
import com.Stefan.BibliotecaUnical.service.ChairService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chair")
@RequiredArgsConstructor
public class ChairController {

    private final ChairService chairService;

    @GetMapping("/{id}")
    public ResponseEntity<ChairFlatDTO> getChairById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(chairService.getChairById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ChairDTO>> getAllChairs(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "8") int size)
    {
        return new ResponseEntity<>(chairService.getAllChairs(page, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChairDTO> saveChair(@RequestBody ChairDTO chairDTO)
    {
        return new ResponseEntity<>(chairService.saveChair(chairDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChair(Long id)
    {
        chairService.deleteChair(id);
        return new ResponseEntity<>(("Chair with id: " + id + " deleted successfully."), HttpStatus.OK);
    }
}
