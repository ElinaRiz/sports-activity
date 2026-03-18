package ru.ssau.management_service.jpa;

import ru.ssau.management_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByRegistrationId(Long activityId);
}