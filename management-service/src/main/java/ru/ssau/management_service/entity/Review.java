package ru.ssau.management_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews", schema = "public")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Registration registration;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;
}