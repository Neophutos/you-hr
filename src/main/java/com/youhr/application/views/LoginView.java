package com.youhr.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
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
 * @author Riccardo Prochnow, Ben Köppe
 * @version 1.0
 * @since 2022-07-20
 */
@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final LoginI18n i18n = LoginI18n.createDefault();

    /**
     * @desc Initialisierung des grafischen Interfaces
     */
    public LoginView(){
        buildUI();
    }

    public void buildUI(){
        addClassName("login-view");
        setSizeFull();
        configureGerman();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.addForgotPasswordListener(event -> Notification.show("Hinweis: Wenden Sie sich an den Systemadmin"));
        login.setI18n(i18n);

        HorizontalLayout top = new HorizontalLayout();
        Image logo = new Image("icons/icon.png","YOU-Logo");
        logo.setMaxWidth("80px");
        Text title = new Text("YOU-HR");
        top.setAlignItems(FlexComponent.Alignment.CENTER);
        top.add(logo, title);

        add(new H1(top), login);
    }

    public void configureGerman(){
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("\uD83D\uDD10 Login");
        i18nForm.setUsername("Nutzername");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Einloggen");
        i18nForm.setForgotPassword("Passwort vergessen?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Falscher Nutzername oder Passwort!");
        i18nErrorMessage.setMessage("Überprüfen Sie, ob der Benutzername und das Passwort korrekt sind und versuchen Sie es erneut.");
        i18n.setErrorMessage(i18nErrorMessage);
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