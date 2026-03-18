package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ActivityTypePojo {
    private long id;
    private String name;
    private int baseDuration;
    private double baseCost;
    private List<ActivityPojo> activityPojos;

    public static ActivityTypePojo fromEntity(ActivityType type) {
        ActivityTypePojo pojo = new ActivityTypePojo();
        pojo.setId(type.getId());
        pojo.setName(type.getName());
        pojo.setBaseDuration(type.getBaseDuration());
        pojo.setBaseCost(type.getBaseCost());

        List<ActivityPojo> activityList = new ArrayList<>();
        pojo.setActivityPojos(activityList);
        for (Activity activity : type.getActivities())
            activityList.add(ActivityPojo.fromEntity(activity));

        return pojo;
    }

    public static ActivityType toEntity(ActivityTypePojo pojo) {
        ActivityType type = new ActivityType();
        type.setId(pojo.getId());
        type.setName(pojo.getName());
        type.setBaseDuration(pojo.getBaseDuration());
        type.setBaseCost(pojo.getBaseCost());

        List<Activity> activityList = new ArrayList<>();
        type.setActivities(activityList);

        return type;
    }
}