package ru.ssau.auth.dto;

import ru.ssau.auth.entity.User;
import ru.ssau.auth.entity.ERole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

        return user;
    }
}