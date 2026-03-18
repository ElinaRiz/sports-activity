package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "halls", schema = "public")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private long id;

    @Column(name = "name",  columnDefinition = "text", length = 100)
    private String name;

    @Column(name = "room", unique = true)
    private int room;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "hall", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Activity> activities;
}