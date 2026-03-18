package ru.ssau.management_service.dto.event;

public record ActivityCreatedEvent(
        long activityId,
        long activityTypeId,
        String activityTypeName,
        int duration,
        double cost,
        int maxParticipants,
        long trainerId,
        String trainerFullName) {
}