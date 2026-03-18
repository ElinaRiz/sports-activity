package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.Trainer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TrainerPojo {
    private long id;
    private long userId;
    private String userName;
    private String specialization;
    private int experience;
    private String achievements;
    private String status;
    private List<ActivityPojo> activityPojos;

    public static TrainerPojo fromEntity(Trainer trainer) {
        TrainerPojo pojo = new TrainerPojo();
        pojo.setId(trainer.getId());
        pojo.setUserId(trainer.getUser().getId());
        pojo.setUserName(trainer.getUser().getFullName());
        pojo.setSpecialization(trainer.getSpecialization());
        pojo.setExperience(trainer.getExperience());
        pojo.setAchievements(trainer.getAchievements());
        pojo.setStatus(trainer.getStatus());

        List<ActivityPojo> activityList = new ArrayList<>();
        pojo.setActivityPojos(activityList);
        for (Activity activity : trainer.getActivities())
            activityList.add(ActivityPojo.fromEntity(activity));

        return pojo;
    }

    public static Trainer toEntity(TrainerPojo pojo) {
        Trainer trainer = new Trainer();
        trainer.setId(pojo.getId());
        trainer.setSpecialization(pojo.getSpecialization());
        trainer.setExperience(pojo.getExperience());
        trainer.setAchievements(pojo.getAchievements());
        trainer.setStatus(pojo.getStatus());

        List<Activity> activityList = new ArrayList<>();
        trainer.setActivities(activityList);

        return trainer;
    }
}