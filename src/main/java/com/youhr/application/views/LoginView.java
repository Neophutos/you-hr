package com.youhr.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
@PageTitle("Login")
public class LoginView extends FlexLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    /**
     * @desc Initialisierung des grafischen Interfaces
     */
    public LoginView(){
        buildUI();
    }

    public void buildUI(){
        setSizeFull();

        login.setAction("login");
        login.addForgotPasswordListener(event -> Notification.show("Hinweis: Wenden Sie sich an ihren Personalbeauftragten"));

        FlexLayout zentriertesLayout = new FlexLayout();
        zentriertesLayout.setSizeFull();
        zentriertesLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        zentriertesLayout.setAlignItems(Alignment.CENTER);
        zentriertesLayout.add(login);

        Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(zentriertesLayout);
    }

    private Component buildLoginInformation(){
        VerticalLayout loginInformation = new VerticalLayout();

        H2 loginInfoHeader = new H2("Willkommen bei YOU-HR!");
        loginInfoHeader.setWidth("80%");
        Span loginInfoText = new Span(
                "Loggen Sie sich als \"admin\", \"personaler\" oder \"mitarbeiter\" " +
                        "ein, um Zugang zum System zu haben. \n" +
                        "Wenn Sie bereits einen neuen Mitarbeiter-Account erstellt haben, " +
                        "nutzen Sie das im Erstellungsprozess erhaltene Passwort!"
        );
        loginInfoText.setWidth("80%");
        loginInformation.add(loginInfoHeader, loginInfoText);

        return loginInformation;
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