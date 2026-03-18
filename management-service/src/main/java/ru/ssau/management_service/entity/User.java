package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "login", columnDefinition = "text", length = 50, unique = true)
    private String login;

    @Column(name = "hash_password", columnDefinition = "text")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "text", length = 20)
    private ERole role;

    @Column(name = "full_name", columnDefinition = "text")
    private String fullName;

    @Column(name = "email", columnDefinition = "text", length = 100, unique = true)
    private String email;

    @Column(name = "phone", columnDefinition = "text", length = 20, unique = true)
    private String phone;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Trainer trainer;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Registration> registrations;
}