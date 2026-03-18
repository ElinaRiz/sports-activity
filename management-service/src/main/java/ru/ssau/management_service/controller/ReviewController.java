package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.ssau.management_service.dto.ReviewPojo;
import ru.ssau.management_service.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<List<ReviewPojo>> findAll() {
        List<ReviewPojo> result = reviewService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewPojo> findById(@PathVariable long id) {
        ReviewPojo result = reviewService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by_activity/{activityId}")
    public ResponseEntity<List<ReviewPojo>> findByActivityId(@PathVariable long activityId) {
        List<ReviewPojo> result = reviewService.findByActivityId(activityId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ReviewPojo> create(@RequestBody ReviewPojo pojo) {
        ReviewPojo result = reviewService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewPojo> update(@PathVariable long id, @RequestBody ReviewPojo pojo) {
        ReviewPojo result = reviewService.update(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}