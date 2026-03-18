package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.management_service.dto.ActivityPojo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ssau.management_service.dto.event.ActivityCreatedEvent;
import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.ActivityType;
import ru.ssau.management_service.entity.Hall;
import ru.ssau.management_service.entity.Trainer;
import ru.ssau.management_service.event_procucers.AnalyticsServiceProducer;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.jpa.ActivityRepository;
import ru.ssau.management_service.jpa.ActivityTypeRepository;
import ru.ssau.management_service.jpa.HallRepository;
import ru.ssau.management_service.jpa.TrainerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityTypeRepository typeRepository;
    private final TrainerRepository trainerRepository;
    private final HallRepository hallRepository;
    private final AnalyticsServiceProducer analyticsClient;

    public List<ActivityPojo> findAll() {
        List<ActivityPojo> result = new ArrayList<>();
        for (Activity activity : activityRepository.findAll(Sort.by(Sort.Direction.DESC, "startTime")))
            result.add(ActivityPojo.fromEntity(activity));
        return result;
    }

    public ActivityPojo findById(long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Занятие не найдено: " + id));
        return ActivityPojo.fromEntity(activity);
    }

    public ActivityPojo create(ActivityPojo pojo) {
        ActivityType type = typeRepository.findById(pojo.getActivityTypeId()).orElseThrow(() -> new EntityNotFoundException("Вид занятий не найден: " + pojo.getActivityTypeId()));
        Trainer trainer = trainerRepository.findById(pojo.getTrainerId()).orElseThrow(() -> new EntityNotFoundException("Тренер не найден: " + pojo.getTrainerId()));
        Hall hall = hallRepository.findById(pojo.getHallId()).orElseThrow(() -> new EntityNotFoundException("Зал не найден: " + pojo.getHallId()));

        Activity activity = ActivityPojo.toEntity(pojo);
        activity.setActivityType(type);
        activity.setTrainer(trainer);
        activity.setHall(hall);

        Activity saved = activityRepository.save(activity);

        analyticsClient.sendActivityCreatedEvent(new ActivityCreatedEvent(saved.getId(), saved.getActivityType().getId(), saved.getActivityType().getName(),
                saved.getDuration(), saved.getCost(), saved.getMaxParticipants(), saved.getTrainer().getId(), saved.getTrainer().getUser().getFullName()));
        return ActivityPojo.fromEntity(saved);
    }

    public ActivityPojo update(long id, ActivityPojo pojo) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Занятие не найдено: " + id));
        ActivityType type = typeRepository.findById(pojo.getActivityTypeId()).orElseThrow(() -> new EntityNotFoundException("Вид занятий не найден: " + pojo.getActivityTypeId()));
        Trainer trainer = trainerRepository.findById(pojo.getTrainerId()).orElseThrow(() -> new EntityNotFoundException("Тренер не найден: " + pojo.getTrainerId()));
        Hall hall = hallRepository.findById(pojo.getHallId()).orElseThrow(() -> new EntityNotFoundException("Зал не найден: " + pojo.getHallId()));

        activity.setName(pojo.getName());
        activity.setActivityLevel(pojo.getActivityLevel());
        activity.setStartTime(pojo.getStartTime());
        activity.setDuration(pojo.getDuration());
        activity.setMaxParticipants(pojo.getMaxParticipants());
        activity.setCost(pojo.getCost());
        activity.setStatus(pojo.getStatus());
        activity.setActivityType(type);
        activity.setTrainer(trainer);
        activity.setHall(hall);

        Activity updated = activityRepository.save(activity);
        return ActivityPojo.fromEntity(updated);
    }

    public void delete(long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Занятие не найдено: " + id));
        try {
            activityRepository.delete(activity);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить занятие");
        }
    }
}