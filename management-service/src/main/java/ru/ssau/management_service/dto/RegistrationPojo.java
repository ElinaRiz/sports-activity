package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Registration;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegistrationPojo {
    private long id;
    private long activityId;
    private String activityName;
    private long userId;
    private String userName;
    private LocalDateTime registrationDate;
    private String paymentStatus;
    private boolean attendanceStatus;
    private ReviewPojo reviewPojo;

    public static RegistrationPojo fromEntity(Registration registration) {
        RegistrationPojo pojo = new RegistrationPojo();
        pojo.setId(registration.getId());
        pojo.setActivityId(registration.getActivity().getId());
        pojo.setActivityName(registration.getActivity().getName());
        pojo.setUserId(registration.getUser().getId());
        pojo.setUserName(registration.getUser().getFullName());
        pojo.setRegistrationDate(registration.getRegistrationDate());
        pojo.setPaymentStatus(registration.getPaymentStatus());
        pojo.setAttendanceStatus(registration.isAttendanceStatus());

        if (registration.getReview() != null) {
            pojo.setReviewPojo(ReviewPojo.fromEntity(registration.getReview()));
        }

        return pojo;
    }

    public static Registration toEntity(RegistrationPojo pojo) {
        Registration registration = new Registration();
        registration.setId(pojo.getId());
        registration.setRegistrationDate(pojo.getRegistrationDate());
        registration.setPaymentStatus(pojo.getPaymentStatus());
        registration.setAttendanceStatus(pojo.isAttendanceStatus());

        registration.setReview(null);

        return registration;
    }
}