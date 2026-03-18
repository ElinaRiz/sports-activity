package ru.ssau.management_service.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.management_service.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Modifying
    @Transactional
    @Query("delete from Trainer t where t.id = :id")
    void deleteByIdCustom(@Param("id") Long id);
}