package com.example.application.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * @desc Der View Login stellt die Zutrittsblockade zum Programm dar. Die Klasse implementiert ein Interface, auf dem Nutzername und Passwort gefordert werden, um fortschreiten zu können (Login-Formular).
 *
 * @see LoginForm
 *
 * @category View
 * @version 0.8
 * @since 2022-06-14
 */
@Route("login")
@PageTitle("Login | YOU LOGIN")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    /**
     * @desc Initialisierung des grafischen Interfaces
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
     * @desc Diese Methode prüft, ob das Ergebnis der Eingaben korrekt ist und wirft einen Fehler bei Ungültigkeit aus.
     * @param beforeEnterEvent
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}