package com.youhr.application.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.youhr.application.data.entity.*;
import com.youhr.application.data.repository.*;
import com.youhr.application.security.Role;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @desc Der DataGenerator erstellt bei erstmaligem Start des Programms generische Rollen, Abteilungen, Teams und Stati für Anträge.
 * @desc Unter generischen Rollen sind Mitarbeiter, Personaler und Admin mit vorgegebenen Rollen zu verstehen.
 *
 * @category Generator
 * @author Tim Freund, Ben Köppe, Riccardo Prochnow
 * @version 1.0
 * @since 2022-08-13
 */
@SpringComponent
public class DataGenerator {

    private static PasswordEncoder passwordEncoder;
    private static UserRepository userRepository;
    private static StatusRepository statusRepository;

    @Bean
    public CommandLineRunner loadData(AbteilungRepository abteilungRepository, TeamRepository teamRepository, StatusRepository statusRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {

        DataGenerator.passwordEncoder = passwordEncoder;
        DataGenerator.userRepository = userRepository;
        DataGenerator.statusRepository = statusRepository;


        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Nutzung der bestehenden Datenbank");
                return;
            }

            logger.info("Generiere Start-Up-Daten");

            logger.info("... generiere Stati für Anträge...");
            List<Status> statuses = statusRepository
                    .saveAll(Stream.of("Offen", "In Bearbeitung", "Zurückgestellt", "Abgeschlossen")
                            .map(Status::new).collect(Collectors.toList()));

            logger.info("... generiere 3 Nutzer (Mitarbeiter, Personaler, Admin)...");
            User user = new User();
            user.setName("Mitarbeiter");
            user.setUsername("mitarbeiter");
            user.setHashedPassword(passwordEncoder.encode("mitarbeiter"));
            user.setProfilePictureUrl("https://img.icons8.com/external-flaticons-lineal-color-flat-icons/344/external-employee-job-search-flaticons-lineal-color-flat-icons-2.png");
            user.setRoles(Collections.singleton(Role.MITARBEITER));
            userRepository.save(user);

            User personaler = new User();
            personaler.setName("Personaler");
            personaler.setUsername("personaler");
            personaler.setHashedPassword(passwordEncoder.encode("personaler"));
            personaler.setProfilePictureUrl("https://img.icons8.com/external-flaticons-flat-flat-icons/344/external-hr-manager-professions-flaticons-flat-flat-icons.png");
            personaler.setRoles(Set.of(Role.MITARBEITER, Role.PERSONALER));
            userRepository.save(personaler);

            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl("https://img.icons8.com/dusk/344/admin-settings-male.png");
            admin.setRoles(Set.of(Role.MITARBEITER, Role.ADMIN));
            userRepository.save(admin);

            Abteilung emptyA = new Abteilung();
            emptyA.setBezeichnung("-");
            abteilungRepository.save(emptyA);

            Abteilung emptyA2 = new Abteilung();
            emptyA2.setBezeichnung("Geschäftsleitung");
            abteilungRepository.save(emptyA2);

            Abteilung emptyA3 = new Abteilung();
            emptyA3.setBezeichnung("EDV / IT");
            abteilungRepository.save(emptyA3);

            Abteilung emptyA4 = new Abteilung();
            emptyA4.setBezeichnung("Vertrieb");
            abteilungRepository.save(emptyA4);

            Abteilung emptyA5 = new Abteilung();
            emptyA5.setBezeichnung("Personalwesen");
            abteilungRepository.save(emptyA5);

            Abteilung emptyA6 = new Abteilung();
            emptyA6.setBezeichnung("Marketing / Werbung");
            abteilungRepository.save(emptyA6);

            Team emptyT = new Team();
            emptyT.setBezeichnung("-");
            teamRepository.save(emptyT);

            Team emptyT2 = new Team();
            emptyT2.setBezeichnung("Innendienst");
            teamRepository.save(emptyT2);

            Team emptyT3 = new Team();
            emptyT3.setBezeichnung("Außendienst");
            teamRepository.save(emptyT3);

            Team emptyT4 = new Team();
            emptyT4.setBezeichnung("Controlling");
            teamRepository.save(emptyT4);

            Team emptyT5 = new Team();
            emptyT5.setBezeichnung("Personalentwicklung");
            teamRepository.save(emptyT5);

            Team emptyT6 = new Team();
            emptyT6.setBezeichnung("IT-Support");
            teamRepository.save(emptyT6);

            logger.info("Generation von Nutzern, Team und Abteilung war erfolgreich!");
        };
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static StatusRepository getStatusRepository() {
        return statusRepository;
    }

}