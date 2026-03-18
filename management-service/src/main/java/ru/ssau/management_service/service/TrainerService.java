package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.TrainerPojo;
import ru.ssau.management_service.entity.Trainer;
import ru.ssau.management_service.entity.User;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.TrainerRepository;
import ru.ssau.management_service.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public List<TrainerPojo> findAll() {
        List<TrainerPojo> result = new ArrayList<>();
        for (Trainer trainer : trainerRepository.findAll(Sort.by("id")))
            result.add(TrainerPojo.fromEntity(trainer));
        return result;
    }

    public TrainerPojo findById(long id) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Тренер не найден: " + id));
        return TrainerPojo.fromEntity(trainer);
    }

    public TrainerPojo create(TrainerPojo pojo) {
        User user = userRepository.findById(pojo.getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + pojo.getUserId()));

        Trainer trainer = TrainerPojo.toEntity(pojo);
        trainer.setUser(user);

        try {
            Trainer saved = trainerRepository.save(trainer);
            return TrainerPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Данные тренера для этого пользователя уже существуют");
        }
    }

    public TrainerPojo update(long id, TrainerPojo pojo) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Тренер не найден: " + id));
        User user = userRepository.findById(pojo.getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + pojo.getUserId()));

        trainer.setSpecialization(pojo.getSpecialization());
        trainer.setExperience(pojo.getExperience());
        trainer.setAchievements(pojo.getAchievements());
        trainer.setStatus(pojo.getStatus());
        trainer.setUser(user);

        try {
            Trainer updated = trainerRepository.save(trainer);
            return TrainerPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: данные тренера для этого пользователя уже существуют");
        }
    }

    public void delete(long id) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Тренер не найден: " + id));
        try {
            trainerRepository.deleteByIdCustom(trainer.getId());
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить тренера: удалить тренерена можно через удаление пользователя");
        }
    }
}