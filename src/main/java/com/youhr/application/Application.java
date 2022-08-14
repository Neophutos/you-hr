package com.youhr.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @desc Der Startpunkt für die Initialisierung und Ausführung der Spring-Boot- und Vaadin-Applikation.
 *
 * @category Application
 * @author Ben Köppe
 * @version 1.0
 * @since 2022-07-10
 */
@SpringBootApplication
@Theme(value = "theoptimistichr")
@PWA(name = "YOU-HR", shortName = "YOU-HR")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

