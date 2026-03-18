package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Activity;
import ru.ssau.management_service.entity.Hall;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HallPojo {
    private long id;
    private String name;
    private int room;
    private int capacity;
    private List<ActivityPojo> activityPojos;

    public static HallPojo fromEntity(Hall hall) {
        HallPojo pojo = new HallPojo();
        pojo.setId(hall.getId());
        pojo.setName(hall.getName());
        pojo.setRoom(hall.getRoom());
        pojo.setCapacity(hall.getCapacity());

        List<ActivityPojo> activityList = new ArrayList<>();
        pojo.setActivityPojos(activityList);
        for (Activity activity : hall.getActivities())
            activityList.add(ActivityPojo.fromEntity(activity));

        return pojo;
    }

    public static Hall toEntity(HallPojo pojo) {
        Hall hall = new Hall();
        hall.setId(pojo.getId());
        hall.setName(pojo.getName());
        hall.setRoom(pojo.getRoom());
        hall.setCapacity(pojo.getCapacity());

        List<Activity> activityList = new ArrayList<>();
        hall.setActivities(activityList);

        return hall;
    }
}
