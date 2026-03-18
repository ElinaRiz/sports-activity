package ru.ssau.management_service.dto;

import ru.ssau.management_service.entity.Registration;
import ru.ssau.management_service.entity.User;
import ru.ssau.management_service.entity.ERole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserPojo {
    private long id;
    private String login;
    private String password;
    private ERole role;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private TrainerPojo trainerPojo;
    private List<RegistrationPojo> registrationPojos;

    public static UserPojo fromEntity(User user) {
        UserPojo pojo = new UserPojo();
        pojo.setId(user.getId());
        pojo.setLogin(user.getLogin());
        pojo.setPassword("");
        pojo.setRole(user.getRole());
        pojo.setFullName(user.getFullName());
        pojo.setEmail(user.getEmail());
        pojo.setPhone(user.getPhone());
        pojo.setBirthDate(user.getBirthDate());

        if (user.getTrainer() != null) {
            pojo.setTrainerPojo(TrainerPojo.fromEntity(user.getTrainer()));
        }

        List<RegistrationPojo> registrationList = new ArrayList<>();
        pojo.setRegistrationPojos(registrationList);
        for (Registration registration : user.getRegistrations())
            registrationList.add(RegistrationPojo.fromEntity(registration));

        return pojo;
    }

    public static User toEntity(UserPojo pojo) {
        User user = new User();
        user.setId(pojo.getId());
        user.setLogin(pojo.getLogin());
        user.setPassword(pojo.getPassword());
        user.setRole(pojo.getRole());
        user.setFullName(pojo.getFullName());
        user.setEmail(pojo.getEmail());
        user.setPhone(pojo.getPhone());
        user.setBirthDate(pojo.getBirthDate());

        user.setTrainer(null);

        List<Registration> registrationList = new ArrayList<>();
        user.setRegistrations(registrationList);

        return user;
    }
}