package com.youhr.application.forms;

import com.youhr.application.data.entity.User;
import com.youhr.application.data.generator.DataGenerator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @desc Das Passwort-Formular ist für die Änderung eines Passworts zuständig. Es besteht aus einem Initialisierungs-, Überprüfungsfeld und eine Bestätigungs-Checkbox.
 *
 * @category Form
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-08-08
 */
public class PasswordForm extends FormLayout {
    private final Binder<User> userBinder = new BeanValidationBinder<>(User.class);

    private static final PasswordEncoder passwordEncoder = DataGenerator.getPasswordEncoder();

    H5 title = new H5("Passwort ändern");

    PasswordField passwort = new PasswordField("Neues Passwort");
    PasswordField checkPasswort = new PasswordField("Passwort bestätigen");

    Checkbox save = new Checkbox("Hiermit bestätige ich, dass ich den betroffenen Mitarbeiter über das neue Passwort informieren werde");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private User selectedUser;

    /**
     * @desc Lesen der Userdaten zum Ausfüllen des Formulars bei Bearbeitung
     * @param selectedUser
     */
    public void setSelectedUser(User selectedUser){
        if(selectedUser != null){
            this.selectedUser = selectedUser;
        }
        userBinder.readBean(selectedUser);
    }

    /**
     * @desc Formular (Text + Eingabefelder + Buttons) wird initialisiert.
     */
    public PasswordForm(){
        addClassName("Passwort-Formular");
        passwort.setMaxWidth("350px");
        checkPasswort.setMaxWidth("350px");
        setMaxWidth("400px");
        add(title, passwort, checkPasswort, save, createButtonLayout());
    }

    /**
     * @desc Konfiguration der Ausrichtung der Buttons und deren Event bei Betätigung (Klick).
     */
    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkAndSave());
        schliessen.addClickListener(event -> fireEvent(new PasswordForm.CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        userBinder.addStatusChangeListener(e -> speichern.setEnabled(userBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung des Users.
     * @error Bei misslungener Speicherung (falsches Passwort oder nicht bestätigt) wird der Nutzer darüber informiert.
     */
    private void checkAndSave() {
        if(Objects.equals(passwort.getValue(), checkPasswort.getValue())) {
            if (save.getValue()) {
                    selectedUser.setHashedPassword(passwordEncoder.encode(passwort.getValue()));
                    fireEvent(new PasswordForm.SaveEvent(this, selectedUser));
                    Notification.show(selectedUser.getName() + " Passwort wurde geändert!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("Sie müssen die Änderung bestätigen!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            Notification.show("Die Passwörter stimmen nicht überein!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class PasswordFormEvent extends ComponentEvent<PasswordForm> {
        private final User user;

        protected PasswordFormEvent(PasswordForm source, User user){
            super(source, false);
            this.user = user;
        }

        public User getUser() { return user;}
    }

    /**
     * @desc Event zur Speicherung der Daten eines Users.
     */
    public static class SaveEvent extends PasswordFormEvent {
        SaveEvent(PasswordForm source, User user) {
            super(source, user);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends PasswordFormEvent {
        CloseEvent(PasswordForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
