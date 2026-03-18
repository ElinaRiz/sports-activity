package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.Registration;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ActivityPojo {
    private long id;
    private String name;
    private long activityTypeId;
    private String activityTypeName;
    private long trainerId;
    private String trainerName;
    private long hallId;
    private int hallRoom;
    private String activityLevel;
    private LocalDateTime startTime;
    private int duration;
    private int maxParticipants;
    private double cost;
    private String status;
    private List<RegistrationPojo> registrationPojos;

    public static ActivityPojo fromEntity(Activity activity) {
        ActivityPojo pojo = new ActivityPojo();
        pojo.setId(activity.getId());
        pojo.setName(activity.getName());
        pojo.setActivityTypeId(activity.getActivityType().getId());
        pojo.setActivityTypeName(activity.getActivityType().getName());
        pojo.setTrainerId(activity.getTrainer().getId());
        pojo.setTrainerName(activity.getTrainer().getUser().getFullName());
        pojo.setHallId(activity.getHall().getId());
        pojo.setHallRoom(activity.getHall().getRoom());
        pojo.setActivityLevel(activity.getActivityLevel());
        pojo.setStartTime(activity.getStartTime());
        pojo.setDuration(activity.getDuration());
        pojo.setMaxParticipants(activity.getMaxParticipants());
        pojo.setCost(activity.getCost());
        pojo.setStatus(activity.getStatus());

        List<RegistrationPojo> registrationList = new ArrayList<>();
        pojo.setRegistrationPojos(registrationList);
        for (Registration registration : activity.getRegistrations())
            registrationList.add(RegistrationPojo.fromEntity(registration));

        return pojo;
    }

    public static Activity toEntity(ActivityPojo pojo) {
        Activity activity = new Activity();
        activity.setId(pojo.getId());
        activity.setName(pojo.getName());
        activity.setActivityLevel(pojo.getActivityLevel());
        activity.setStartTime(pojo.getStartTime());
        activity.setDuration(pojo.getDuration());
        activity.setMaxParticipants(pojo.getMaxParticipants());
        activity.setCost(pojo.getCost());
        activity.setStatus(pojo.getStatus());

        List<Registration> registrationList = new ArrayList<>();
        activity.setRegistrations(registrationList);

        return activity;
    }
}