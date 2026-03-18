package ru.ssau.management_service.jpa;

import ru.ssau.management_service.entity.ERole;
import ru.ssau.management_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(ERole role);
}