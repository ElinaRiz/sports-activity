package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPojo {
    private long id;
    private long userId;
    private String userName;
    private long registrationId;
    private String registrationActivityName;
    private Integer rating;
    private String comment;

    public static ReviewPojo fromEntity(Review review) {
        ReviewPojo pojo = new ReviewPojo();
        pojo.setId(review.getId());
        pojo.setUserId(review.getRegistration().getUser().getId());
        pojo.setUserName(review.getRegistration().getUser().getFullName());
        pojo.setRegistrationId(review.getRegistration().getId());
        pojo.setRegistrationActivityName(review.getRegistration().getActivity().getName());
        pojo.setRating(review.getRating());
        pojo.setComment(review.getComment());

        return pojo;
    }

    public static Review toEntity(ReviewPojo pojo) {
        Review review = new Review();
        review.setId(pojo.getId());
        review.setRating(pojo.getRating());
        review.setComment(pojo.getComment());

        return review;
    }
}