package ru.ssau.management_service.jpa;

import ru.ssau.management_service.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}