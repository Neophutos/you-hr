package com.example.application.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Der View Login stellt die Zutrittsblockade zum Programm dar.
 * Die Klasse implementiert ein Interface, auf dem Nutzername und
 * Passwort gefordert werden, um fortschreiten zu können (Login-Formular).
 *
 * @see LoginForm
 *
 * @author Ben Köppe, Riccardo Prochnow
 * @version 0.8
 * @since 2022-06-14
 */
@Route("login")
@PageTitle("Login | YOU LOGIN")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    /**
     * Diese Methode initialisiert das grafische Interface
     * und fügt diese zum View hinzu.
     */
    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        add(new H1("YOU LOGIN"), login);
    }

    /**
     * Diese Methode prüft, ob das Ergebnis der Eingaben korrekt ist
     * und wirft einen Fehler bei Ungültigkeit aus.
     *
     * @param beforeEnterEvent
     */
    @Override
    //Prüft ob das die Eingabe korrekt sind, und wirft ein Fehler bei Ungültigkeit aus
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}