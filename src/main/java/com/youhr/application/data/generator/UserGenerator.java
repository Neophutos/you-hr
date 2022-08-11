package com.youhr.application.data.generator;

import com.youhr.application.security.Role;
import com.youhr.application.data.entity.User;
import com.youhr.application.data.service.UserService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Set;

/**
 * @desc Die Klasse UserGenerator erstellt das zum Mitarbeiter verbundene Nutzerkonto.
 * @desc Dabei ist sie außerdem für das Hashing von erstellten bzw. veränderten Passwörten verantwortlich.
 *
 * @category Generator
 * @author Tim Freund
 * @version 1.0
 * @since 2022-08-05
 */
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
        Notification.show("Initialisierungs-Passwort: " + pw).setDuration(10);
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
