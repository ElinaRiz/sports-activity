package ru.ssau.management_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.ssau.management_service.dto.UserPojo;
import ru.ssau.management_service.entity.ERole;
import ru.ssau.management_service.entity.User;
import ru.ssau.management_service.exception.DeleteException;
import ru.ssau.management_service.exception.DuplicateNameException;
import ru.ssau.management_service.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserPojo> findAll() {
        List<UserPojo> result = new ArrayList<>();
        for (User user : userRepository.findAll(Sort.by("id")))
            result.add(UserPojo.fromEntity(user));
        return result;
    }

    public List<UserPojo> findAllTrainers() {
        List<UserPojo> result = new ArrayList<>();
        for (User user : userRepository.findByRole(ERole.TRAINER))
            result.add(UserPojo.fromEntity(user));
        return result;
    }

    public List<UserPojo> findAllUsers() {
        List<UserPojo> result = new ArrayList<>();
        for (User user : userRepository.findByRole(ERole.USER))
            result.add(UserPojo.fromEntity(user));
        return result;
    }

    public UserPojo findById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + id));
        return UserPojo.fromEntity(user);
    }

    public UserPojo create(UserPojo pojo) {
        User user = UserPojo.toEntity(pojo);
        user.setPassword(passwordEncoder.encode(pojo.getPassword()));

        try {
            User saved = userRepository.save(user);
            return UserPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Пользователь с таким логином, почтой и телефоном уже существует");
        }
    }

    public UserPojo updateWithoutPass(long id, UserPojo pojo) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + id));

        user.setLogin(pojo.getLogin());
        user.setRole(pojo.getRole());
        user.setFullName(pojo.getFullName());
        user.setEmail(pojo.getEmail());
        user.setPhone(pojo.getPhone());
        user.setBirthDate(pojo.getBirthDate());

        try {
            User updated = userRepository.save(user);
            return UserPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Конфликт данных: пользователь с таким логином, почтой и телефоном уже существует");
        }
    }

    public UserPojo updatePassword(long id, UserPojo pojo) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + id));

        user.setPassword(passwordEncoder.encode(pojo.getPassword()));

        try {
            User updated = userRepository.save(user);
            return UserPojo.fromEntity(updated);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Ошибка обновления пароля");
        }
    }

    public void delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + id));
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new DeleteException("Не получилось удалить пользователя");
        }
    }
}