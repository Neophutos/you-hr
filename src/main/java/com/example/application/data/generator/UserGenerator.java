package com.example.application.data.generator;

import com.example.application.data.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UserGenerator {

    private static PasswordEncoder passwordEncoder = DataGenerator.getPasswordEncoder();
    private static UserService userService = new UserService(DataGenerator.getUserRepository());

    public static User generate(String vorname, String nachname, User user) {
        user.setName(String.format("%s %s", vorname, nachname));
        user.setUsername(String.format("%s.%s", vorname, nachname));
        user.setHashedPassword(passwordEncoder.encode(String.format("%s.%s", vorname, nachname)));

        user.setRoles(Set.of(Role.USER));

        int results = (int) DataGenerator
                .getUserRepository()
                .findAll()
                .stream()
                .filter(u -> u.getUsername() == user.getUsername())
                .count();

        if (results != 0) {
            System.out.println("User does already exists");
        }

    return user;
    }
}
