package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.ReviewPojo;
import ru.ssau.management_service.entity.Registration;
import ru.ssau.management_service.entity.Review;
import ru.ssau.management_service.event_procucers.RabbitProducer;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.RegistrationRepository;
import ru.ssau.management_service.jpa.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RegistrationRepository registrationRepository;
    private final RabbitProducer rabbitProducer;

    public List<ReviewPojo> findAll() {
        List<ReviewPojo> result = new ArrayList<>();
        for (Review review : reviewRepository.findAll(Sort.by("id")))
            result.add(ReviewPojo.fromEntity(review));
        return result;
    }

    public ReviewPojo findById(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Отзыв не найден: " + id));
        return ReviewPojo.fromEntity(review);
    }

    public List<ReviewPojo> findByActivityId(long activityId) {
        List<ReviewPojo> result = new ArrayList<>();
        for (Registration reg : registrationRepository.findByActivity_Id(activityId, Sort.by("id"))) {
            Review r = reviewRepository.findByRegistrationId(reg.getId());
            if (r != null) {
                result.add(ReviewPojo.fromEntity(r));
            }
        }
        return result;
    }

    public ReviewPojo create(ReviewPojo pojo) {
        Registration registration = registrationRepository.findById(pojo.getRegistrationId()).orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + pojo.getRegistrationId()));

        Review review = ReviewPojo.toEntity(pojo);
        review.setRegistration(registration);

        try {
            Review saved = reviewRepository.save(review);

            Map<String, Object> data = new HashMap<>();
            data.put("activityId", saved.getRegistration().getActivity().getId());
            data.put("activityName", saved.getRegistration().getActivity().getName());
            data.put("rating", saved.getRating());
            data.put("trainerId", saved.getRegistration().getActivity().getTrainer().getId());
            data.put("trainerFullName", saved.getRegistration().getActivity().getTrainer().getUser().getFullName());
            data.put("userId", saved.getRegistration().getUser().getId());
            data.put("userLogin", saved.getRegistration().getUser().getLogin());
            rabbitProducer.sendEvent("review_created", data);

            return ReviewPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Отзыв для данной регистрации уже существует");
        }
    }

    public ReviewPojo update(long id, ReviewPojo pojo) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Отзыв не найден: " + id));

        Registration registration = registrationRepository.findById(pojo.getRegistrationId()).orElseThrow(() -> new EntityNotFoundException("Регистрация не найдена: " + pojo.getRegistrationId()));

        review.setRating(pojo.getRating());
        review.setComment(pojo.getComment());
        review.setRegistration(registration);

        try {
            Review updated = reviewRepository.save(review);
            return ReviewPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: отзыв для данной регистрации уже существует");
        }
    }

    public void delete(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Отзыв не найден: " + id));
        try {
            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить отзыв");
        }
    }
}