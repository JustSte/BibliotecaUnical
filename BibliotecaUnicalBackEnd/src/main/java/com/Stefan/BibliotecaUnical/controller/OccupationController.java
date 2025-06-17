package com.Stefan.BibliotecaUnical.controller;


import com.Stefan.BibliotecaUnical.service.OccupyResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupy")
public class OccupationController {

    private final OccupyResourceService occupyResourceService;

    @PostMapping("/chair/{id}")
    public ResponseEntity<Void> occupyChair(@PathVariable Long id)
    {
        occupyResourceService.occupyChair(id, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locker/{id}")
    public ResponseEntity<Void> occupyLocker(@PathVariable Long id)
    {
        occupyResourceService.occupyLocker(id, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chair/free/{id}")
    public ResponseEntity<Void> freeChair(@PathVariable Long id)
    {
        occupyResourceService.freeChair(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locker/free/{id}")
    public ResponseEntity<Void> freeLocker(@PathVariable Long id)
    {
        occupyResourceService.freeLocker(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/freeAllLockers")
    public ResponseEntity<Void> freeAllLockers()
    {
        occupyResourceService.freeAllLockers();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/freeAllChairs")
    public ResponseEntity<Void> freeAllChairs()
    {
        occupyResourceService.freeAllChairs();
        return ResponseEntity.ok().build();
    }
}
