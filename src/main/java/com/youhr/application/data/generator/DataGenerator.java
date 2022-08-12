package com.youhr.application.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Mitarbeiter;
import com.youhr.application.data.entity.Team;
import com.youhr.application.data.repository.AbteilungRepository;
import com.youhr.application.data.repository.TeamRepository;
import com.youhr.application.security.Role;
import com.youhr.application.data.entity.User;
import com.youhr.application.data.repository.MitarbeiterRepository;
import com.youhr.application.data.repository.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    private static PasswordEncoder passwordEncoder;
    private static UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData(AbteilungRepository abteilungRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;


        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 3 User (Mitarbeiter, Personaler, Admin) entities...");
            User user = new User();
            user.setName("Test-User");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://cdn-icons-png.flaticon.com/512/2405/2405283.png");
            user.setRoles(Collections.singleton(Role.MITARBEITER));
            userRepository.save(user);

            User personaler = new User();
            personaler.setName("Personaler");
            personaler.setUsername("personaler");
            personaler.setHashedPassword(passwordEncoder.encode("personaler"));
            personaler.setProfilePictureUrl("https://cdn-icons.flaticon.com/png/512/1886/premium/1886937.png?token=exp=1659079826~hmac=ec6e9ea6d5a7512c443aa19ef0b9c5f7");
            personaler.setRoles(Set.of(Role.MITARBEITER, Role.PERSONALER));
            userRepository.save(personaler);

            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl("https://cdn-icons.flaticon.com/png/512/6024/premium/6024190.png?token=exp=1659010722~hmac=30e6cdc61ba60b839187d66d894235f0");
            admin.setRoles(Set.of(Role.MITARBEITER, Role.ADMIN));
            userRepository.save(admin);

            Abteilung emptyA = new Abteilung();
            emptyA.setBezeichnung("-");
            abteilungRepository.save(emptyA);

            Team emptyT = new Team();
            emptyT.setBezeichnung("-");
            teamRepository.save(emptyT);

            logger.info("Generated accounts and non-Abteilungen and -Teams!");
        };
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }
}