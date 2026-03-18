package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.ssau.management_service.dto.HallPojo;
import ru.ssau.management_service.service.HallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
public class HallController {
    private final HallService hallService;

    @PreAuthorize("hasRole('ADMIN') || hasRole('TRAINER')")
    @GetMapping("/all")
    public ResponseEntity<List<HallPojo>> findAll() {
        List<HallPojo> result = hallService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HallPojo> findById(@PathVariable long id) {
        HallPojo result = hallService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<HallPojo> create(@RequestBody HallPojo pojo) {
        HallPojo result = hallService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HallPojo> update(@PathVariable long id, @RequestBody HallPojo pojo) {
        HallPojo result = hallService.update(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }
}