package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.ChairDTOs.ChairDTO;
import com.Stefan.BibliotecaUnical.service.ChairService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chair")
@RequiredArgsConstructor
public class ChairController {

    private final ChairService chairService;

    @GetMapping("/{id}")
    public ResponseEntity<ChairDTO> getChairById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(chairService.getChairById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ChairDTO>> getAllChairs()
    {
        return new ResponseEntity<>(chairService.getAllChairs(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChairDTO> saveChair(@RequestBody ChairDTO chairDTO)
    {
        return new ResponseEntity<>(chairService.saveChair(chairDTO), HttpStatus.CREATED);
    }
}
