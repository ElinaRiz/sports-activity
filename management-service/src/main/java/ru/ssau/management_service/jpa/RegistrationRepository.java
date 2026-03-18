package ru.ssau.management_service.jpa;

import org.springframework.data.domain.Sort;
import ru.ssau.management_service.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByActivity_Id(long activityId, Sort sort);
    Registration findByUser_IdAndActivity_Id(long userId, long activityId);
}