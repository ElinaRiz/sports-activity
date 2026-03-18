package ru.ssau.management_service.controller;

import lombok.RequiredArgsConstructor;
import ru.ssau.management_service.dto.UserPojo;
import ru.ssau.management_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN') || hasRole('TRAINER')")
    @GetMapping("/all")
    public ResponseEntity<List<UserPojo>> findAll() {
        List<UserPojo> result = userService.findAll();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('TRAINER')")
    @GetMapping("/all_users")
    public ResponseEntity<List<UserPojo>> findAllUser() {
        List<UserPojo> result = userService.findAllUsers();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all_trainers")
    public ResponseEntity<List<UserPojo>> findAllTrainers() {
        List<UserPojo> result = userService.findAllTrainers();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPojo> findById(@PathVariable long id) {
        UserPojo result = userService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<UserPojo> create(@RequestBody UserPojo pojo) {
        UserPojo result = userService.create(pojo);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPojo> update(@PathVariable long id, @RequestBody UserPojo pojo) {
        UserPojo result = userService.updateWithoutPass(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/pass/{id}")
    public ResponseEntity<UserPojo> updatePassword(@PathVariable long id, @RequestBody UserPojo pojo) {
        UserPojo result = userService.updatePassword(id, pojo);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}