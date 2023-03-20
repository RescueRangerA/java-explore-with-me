package ru.practicum.ewm.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getAllByIdIn(List<Long> ids);
}
