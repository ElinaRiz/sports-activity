package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "activity_types", schema = "public")
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_type_id")
    private long id;

    @Column(name = "name", columnDefinition = "text", length = 100, unique = true)
    private String name;

    @Column(name = "base_duration")
    private int baseDuration;

    @Column(name = "base_cost")
    private double baseCost;

    @OneToMany(mappedBy = "activityType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Activity> activities;
}