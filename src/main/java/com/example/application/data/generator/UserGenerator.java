package com.example.application.data.generator;

import com.example.application.data.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.notification.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Set;

public class UserGenerator {

    private static PasswordEncoder passwordEncoder = DataGenerator.getPasswordEncoder();
    private static UserService userService = new UserService(DataGenerator.getUserRepository());

    private final Notification notification = new Notification();

    public static User generate(String vorname, String nachname, User user) {
        user.setName(String.format("%s %s", vorname, nachname));
        user.setUsername(String.format("%s.%s", vorname, nachname));
        String pw = generateRandomPassword(10);

        user.setHashedPassword(passwordEncoder.encode(pw));

        user.setRoles(Set.of(Role.MITARBEITER));

        int results = (int) DataGenerator
                .getUserRepository()
                .findAll()
                .stream()
                .filter(u -> u.getUsername() == user.getUsername())
                .count();

        if (results != 0) {
            System.out.println("User does already exists");
        } else {
            userService.update(user);
            System.out.println("user login successfully generated!");
        }

    return user;
    }

    public static String generateHashedPassword() {
        String pw = generateRandomPassword(10);
        Notification.show("Erstelltes Passwort: " + pw).setDuration(0);
        return passwordEncoder.encode(pw);
    }


    private static String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

}
