package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import ru.ssau.management_service.dto.RegistrationPojo;
import ru.ssau.management_service.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/all")
    public ResponseEntity<List<RegistrationPojo>> findAll() {
        List<RegistrationPojo> result = registrationService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationPojo> findById(@PathVariable long id) {
        RegistrationPojo result = registrationService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}/activity/{activityId}")
    public ResponseEntity<RegistrationPojo> findByUserIdAndActivityId(@PathVariable long userId, @PathVariable long activityId) {
        RegistrationPojo result = registrationService.findByUserIdAndActivityId(userId, activityId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by_activity/{activityId}")
    public ResponseEntity<List<RegistrationPojo>> findByActivityId(@PathVariable long activityId) {
        List<RegistrationPojo> result = registrationService.findByActivityId(activityId);
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<RegistrationPojo> create(@RequestBody RegistrationPojo pojo) {
        RegistrationPojo result = registrationService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationPojo> update(@PathVariable long id, @RequestBody RegistrationPojo pojo) {
        RegistrationPojo result = registrationService.update(id, pojo);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        registrationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}