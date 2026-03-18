package ru.ssau.management_service.jpa;

import ru.ssau.management_service.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {
}