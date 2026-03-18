package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.HallPojo;
import ru.ssau.management_service.entity.Hall;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    private final HallRepository hallRepository;

    public List<HallPojo> findAll() {
        List<HallPojo> result = new ArrayList<>();
        for (Hall hall : hallRepository.findAll(Sort.by("id")))
            result.add(HallPojo.fromEntity(hall));
        return result;
    }

    public HallPojo findById(long id) {
        Hall hall = hallRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Зал не найден: " + id));
        return HallPojo.fromEntity(hall);
    }

    public HallPojo create(HallPojo pojo) {
        try {
            Hall saved = hallRepository.save(HallPojo.toEntity(pojo));
            return HallPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Зал с номером " + pojo.getRoom() + " уже существует");
        }
    }

    public HallPojo update(long id, HallPojo pojo) {
        Hall hall = hallRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Зал не найден: " + id));

        hall.setName(pojo.getName());
        hall.setRoom(pojo.getRoom());
        hall.setCapacity(pojo.getCapacity());

        try {
            Hall updated = hallRepository.save(hall);
            return HallPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: зал с номером " + pojo.getRoom() + " уже существует");
        }
    }

    public void delete(long id) {
        Hall hall = hallRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Зал не найден: " + id));
        try {
            hallRepository.delete(hall);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить зал");
        }
    }
}