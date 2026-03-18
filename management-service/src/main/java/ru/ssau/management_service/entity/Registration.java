package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "registrations", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"activity_id", "user_id"}))
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "payment_status", columnDefinition = "text", length = 20)
    private String paymentStatus;

    @Column(name = "attendance_status")
    private boolean attendanceStatus;

    @OneToOne(mappedBy = "registration", cascade = CascadeType.ALL)
    private Review review;
}