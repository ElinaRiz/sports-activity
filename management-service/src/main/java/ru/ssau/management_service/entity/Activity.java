package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "activities", schema = "public")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private long id;

    @Column(name = "name",  columnDefinition = "text", length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_type_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ActivityType activityType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hall hall;

    @Column(name = "activity_level", columnDefinition = "text", length = 20)
    private String activityLevel;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "duration")
    private int duration;

    @Column(name = "max_participants")
    private int maxParticipants;

    @Column(name = "cost")
    private double cost;

    @Column(name = "status", columnDefinition = "text", length = 20)
    private String status;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Registration> registrations;
}