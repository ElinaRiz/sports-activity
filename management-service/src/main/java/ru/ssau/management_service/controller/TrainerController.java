package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.ssau.management_service.dto.TrainerPojo;
import ru.ssau.management_service.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/all")
    public ResponseEntity<List<TrainerPojo>> findAll() {
        List<TrainerPojo> result = trainerService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerPojo> findById(@PathVariable long id) {
        TrainerPojo result = trainerService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TrainerPojo> create(@RequestBody TrainerPojo pojo) {
        TrainerPojo result = trainerService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('TRAINER')")
    @PutMapping("/{id}")
    public ResponseEntity<TrainerPojo> update(@PathVariable long id, @RequestBody TrainerPojo pojo) {
        TrainerPojo result = trainerService.update(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        trainerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}