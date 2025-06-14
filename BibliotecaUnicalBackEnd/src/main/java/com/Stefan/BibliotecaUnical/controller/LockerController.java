package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.request.ModifyLockerRequest;
import com.Stefan.BibliotecaUnical.service.LockerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locker")
public class LockerController {

    private final LockerService lockerService;

    @GetMapping("/{id}")
    public ResponseEntity<LockerDTO> getLockerById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(lockerService.getLockerById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LockerDTO>> getAllLockers()
    {
        return new ResponseEntity<>(lockerService.getAllLockers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LockerDTO> saveLocker(@RequestBody LockerDTO lockerDTO)
    {
        return new ResponseEntity<>(lockerService.saveLocker(lockerDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<LockerDTO> updateLocker(@RequestBody ModifyLockerRequest request)
    {
        return new ResponseEntity<>(lockerService.updateLocker(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocker(@PathVariable @NotNull Long id)
    {
        lockerService.deleteLocker(id);
        return new ResponseEntity<>(("Locker with id: " + id + " successfully deleted."),HttpStatus.OK);
    }
}
