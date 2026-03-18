package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.ssau.management_service.dto.ActivityTypePojo;
import ru.ssau.management_service.service.ActivityTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity_types")
@RequiredArgsConstructor
public class ActivityTypeController {
    private final ActivityTypeService activityTypeService;

    @GetMapping("/all")
    public ResponseEntity<List<ActivityTypePojo>> findAll() {
        List<ActivityTypePojo> result = activityTypeService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityTypePojo> findById(@PathVariable long id) {
        ActivityTypePojo result = activityTypeService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ActivityTypePojo> create(@RequestBody ActivityTypePojo pojo) {
        ActivityTypePojo result = activityTypeService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ActivityTypePojo> update(@PathVariable long id, @RequestBody ActivityTypePojo pojo) {
        ActivityTypePojo result = activityTypeService.update(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable long id) {
        activityTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}