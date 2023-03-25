package ru.practicum.ewm.exception.generic;

import ru.practicum.ewm.model.*;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

public class ExtendedEntityNotFoundException extends EntityNotFoundException {
    private static final Map<Class<?>, String> humunizeMap = Map.of(
            Event.class, "Event",
            EventParticipationRequest.class, "Request",
            EventCategory.class, "Category",
            EventCompilation.class, "Compilation",
            User.class, "User"
    );

    public static ExtendedEntityNotFoundException ofEntity(Class<?> entityClass, Long entityId) {
        String humanizedName = humunizeMap.get(entityClass);
        if (humanizedName == null) {
            humanizedName = entityClass.getName();
        }

        return new ExtendedEntityNotFoundException(
                String.format("%s with id=%d was not found", humanizedName, entityId)
        );
    }

    public <T> ExtendedEntityNotFoundException(String message) {
        super(message);
    }
}
