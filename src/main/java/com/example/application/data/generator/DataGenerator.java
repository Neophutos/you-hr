package com.example.application.data.generator;

import com.example.application.data.Role;
import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Rechteverwaltung;
import com.example.application.data.entity.User;
import com.example.application.data.repository.MitarbeiterRepository;
import com.example.application.data.repository.RechteverwaltungRepository;
import com.example.application.data.repository.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
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
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            MitarbeiterRepository mitarbeiterRepository, RechteverwaltungRepository rechteverwaltungRepository) {

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

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Set.of(Role.USER, Role.ADMIN));
            userRepository.save(admin);
            logger.info("... generating 100 Mitarbeiter entities...");
//            ExampleDataGenerator<Mitarbeiter> mitarbeiterRepositoryGenerator = new ExampleDataGenerator<>(
//                    Mitarbeiter.class, LocalDateTime.of(2022, 5, 30, 0, 0, 0));
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setVorname, DataType.FIRST_NAME);
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setNachname, DataType.LAST_NAME);
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setEmail, DataType.EMAIL);
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setTelefonnr, DataType.PHONE_NUMBER);
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setPosition, DataType.OCCUPATION);
//            mitarbeiterRepositoryGenerator.setData(Mitarbeiter::setAbteilung, DataType.WORD);
//            mitarbeiterRepository.saveAll(mitarbeiterRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Rechteverwaltung entities...");
            {
                {
//                    ExampleDataGenerator<Rechteverwaltung> rechteverwaltungRepositoryGenerator = new ExampleDataGenerator<>(
//                            Rechteverwaltung.class, LocalDateTime.of(2022, 5, 30, 0, 0, 0));
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setVorname, DataType.FIRST_NAME);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setNachname, DataType.LAST_NAME);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setMitarbeiterid, DataType.EAN13);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setLesen, DataType.BOOLEAN_50_50);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setErstellen, DataType.BOOLEAN_50_50);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setBearbeiten, DataType.BOOLEAN_50_50);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setLoeschen, DataType.BOOLEAN_50_50);
//                    rechteverwaltungRepositoryGenerator.setData(Rechteverwaltung::setAdmin, DataType.BOOLEAN_50_50);
//                    rechteverwaltungRepository.saveAll(rechteverwaltungRepositoryGenerator.create(100, seed));
                }
            }

            logger.info("Generated demo data");
        };
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }
}