package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "trainers", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "specialization", columnDefinition = "text")
    private String specialization;

    @Column(name = "experience")
    private int experience;

    @Column(name = "achievements", columnDefinition = "text")
    private String achievements;

    @Column(name = "status", columnDefinition = "text", length = 20)
    private String status;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Activity> activities;
}