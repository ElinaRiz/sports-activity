package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.RegistrationPojo;
import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.Registration;
import ru.ssau.management_service.entity.User;
import ru.ssau.management_service.event_procucers.RabbitProducer;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.ActivityRepository;
import ru.ssau.management_service.jpa.RegistrationRepository;
import ru.ssau.management_service.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RabbitProducer rabbitProducer;

    public List<RegistrationPojo> findAll() {
        List<RegistrationPojo> result = new ArrayList<>();
        for (Registration reg : registrationRepository.findAll(Sort.by("id")))
            result.add(RegistrationPojo.fromEntity(reg));
        return result;
    }

    public RegistrationPojo findById(long id) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + id));
        return RegistrationPojo.fromEntity(registration);
    }

    public RegistrationPojo findByUserIdAndActivityId(long userId, long activityId) {
        try {
            Registration registration = registrationRepository.findByUser_IdAndActivity_Id(userId,  activityId);
            return RegistrationPojo.fromEntity(registration);
        } catch (Exception e) {
            throw new EntityNotFoundException("Регистрация не найдена для пользователя: " + userId);
        }
    }

    public List<RegistrationPojo> findByActivityId(long activityId) {
        List<RegistrationPojo> result = new ArrayList<>();
        for (Registration reg : registrationRepository.findByActivity_Id(activityId, Sort.by("id")))
            result.add(RegistrationPojo.fromEntity(reg));
        return result;
    }

    public RegistrationPojo create(RegistrationPojo pojo) {
        User user = userRepository.findById(pojo.getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + pojo.getUserId()));
        Activity activity = activityRepository.findById(pojo.getActivityId()).orElseThrow(() -> new EntityNotFoundException("Занятие не найдено: " + pojo.getActivityId()));

        Registration registration = RegistrationPojo.toEntity(pojo);
        registration.setUser(user);
        registration.setActivity(activity);

        try {
            Registration saved = registrationRepository.save(registration);

            Map<String, Object> data = new HashMap<>();
            data.put("activityTypeId", saved.getActivity().getActivityType().getId());
            data.put("activityTypeName", saved.getActivity().getActivityType().getName());
            data.put("activityId", saved.getActivity().getId());
            data.put("activityName", saved.getActivity().getName());
            data.put("trainerId", saved.getActivity().getTrainer().getId());
            data.put("trainerFullName", saved.getActivity().getTrainer().getUser().getFullName());
            data.put("userId", saved.getUser().getId());
            data.put("userLogin", saved.getUser().getLogin());
            rabbitProducer.sendEvent("registration_created", data);

            return RegistrationPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Пользователь уже зарегистрировался на это занаятие");
        }
    }

    public RegistrationPojo update(long id, RegistrationPojo pojo) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + id));
        User user = userRepository.findById(pojo.getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + pojo.getUserId()));
        Activity activity = activityRepository.findById(pojo.getActivityId()).orElseThrow(() -> new EntityNotFoundException("Занятие не найдено: " + pojo.getActivityId()));

        registration.setRegistrationDate(pojo.getRegistrationDate());
        registration.setPaymentStatus(pojo.getPaymentStatus());
        registration.setAttendanceStatus(pojo.isAttendanceStatus());
        registration.setUser(user);
        registration.setActivity(activity);

        try {
            Registration updated = registrationRepository.save(registration);
            return RegistrationPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: пользователь уже зарегистрировался на это занаятие");
        }
    }

    public void delete(long id) {
        Registration registration = registrationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + id));
        try {
            registrationRepository.delete(registration);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить регистрацию");
        }
    }
}
