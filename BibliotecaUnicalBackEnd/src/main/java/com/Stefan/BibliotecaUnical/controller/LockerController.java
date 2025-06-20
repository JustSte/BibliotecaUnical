package com.Stefan.BibliotecaUnical.controller;

import com.Stefan.BibliotecaUnical.DTO.LockerDTOs.LockerDTO;
import com.Stefan.BibliotecaUnical.request.ModifyLockerRequest;
import com.Stefan.BibliotecaUnical.service.LockerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locker")
public class LockerController {

    private final LockerService lockerService;

    @PreAuthorize("hasAnyRole('USER' ,'STAFF', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LockerDTO> getLockerById(@PathVariable @NotNull Long id)
    {
        return new ResponseEntity<>(lockerService.getLockerById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER' ,'STAFF', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<LockerDTO>> getAllLockers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size)
    {
        return new ResponseEntity<>(lockerService.getAllLockers(page,size), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LockerDTO> saveLocker(@RequestBody LockerDTO lockerDTO)
    {
        return new ResponseEntity<>(lockerService.saveLocker(lockerDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<LockerDTO> updateLocker(@RequestBody ModifyLockerRequest request)
    {
        return new ResponseEntity<>(lockerService.updateLocker(request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocker(@PathVariable @NotNull Long id)
    {
        lockerService.deleteLocker(id);
        return new ResponseEntity<>(("Locker with id: " + id + " successfully deleted."),HttpStatus.OK);
    }
}
