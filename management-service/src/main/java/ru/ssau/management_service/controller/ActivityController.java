package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.ssau.management_service.dto.ActivityPojo;
import ru.ssau.management_service.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/all")
    public ResponseEntity<List<ActivityPojo>> findAll() {
        List<ActivityPojo> result = activityService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityPojo> findById(@PathVariable long id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ActivityPojo> create(@RequestBody ActivityPojo pojo) {
        ActivityPojo result = activityService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ActivityPojo> update(@PathVariable long id,
                                               @RequestBody ActivityPojo pojo) {
        return ResponseEntity.ok(activityService.update(id, pojo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}