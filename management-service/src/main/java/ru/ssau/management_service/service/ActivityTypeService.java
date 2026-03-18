package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.ActivityTypePojo;
import ru.ssau.management_service.entity.ActivityType;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.ActivityTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityTypeService {
    private final ActivityTypeRepository typeRepository;

    public List<ActivityTypePojo> findAll() {
        List<ActivityTypePojo> result = new ArrayList<>();
        for (ActivityType type : typeRepository.findAll(Sort.by("id")))
            result.add(ActivityTypePojo.fromEntity(type));
        return result;
    }

    public ActivityTypePojo findById(long id) {
        ActivityType type = typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Вид занятий не найден: " + id));
        return ActivityTypePojo.fromEntity(type);
    }

    public ActivityTypePojo create(ActivityTypePojo pojo) {
        try {
            ActivityType saved = typeRepository.save(ActivityTypePojo.toEntity(pojo));
            return ActivityTypePojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Вид занятий с именем '" + pojo.getName() + "' уже существует");
        }
    }

    public ActivityTypePojo update(long id, ActivityTypePojo pojo) {
        ActivityType type = typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Вид занятий не найден: " + id));

        type.setName(pojo.getName());
        type.setBaseDuration(pojo.getBaseDuration());
        type.setBaseCost(pojo.getBaseCost());

        try {
            ActivityType updated = typeRepository.save(type);
            return ActivityTypePojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: вид занятий с именем '" + pojo.getName() + "' уже существует");
        }
    }

    public void delete(long id) {
        ActivityType type = typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Вид занятий не найден: " + id));
        try {
            typeRepository.delete(type);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить вид занятия");
        }
    }
}