package com.example.application.views.rechteverwaltung;

import com.example.application.security.Role;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class RechteForm extends FormLayout {
    private final Binder<User> userBinder = new BeanValidationBinder<>(User.class);

    H5 title = new H5("Berechtigungen bearbeiten");

    TextField username = new TextField("Username");
    CheckboxGroup<Role> rechteCheck = new CheckboxGroup<>("Berechtigungen");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private User selectedUser;

    public void setSelectedUser(User selectedUser){
        if(selectedUser == null){
            this.selectedUser = new User();
        } else {
            this.selectedUser = selectedUser;
        }

        userBinder.readBean(selectedUser);
    }

    public RechteForm(){
        addClassName("Rechte-Formular");

        rechteCheck.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        rechteCheck.setItems(Role.values());

        userBinder.forField(username)
                .asRequired("Sie müssen einen Username angeben")
                .bind("username");
        userBinder.forField(rechteCheck)
                .asRequired("Wählen Sie mind. eine Berechtigung aus")
                .bind(User::getRoles, User::setRoles);

        setMaxWidth("400px");

        add(title, username, rechteCheck, createButtonLayout());
    }

    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkAndSave());
        schliessen.addClickListener(event -> fireEvent(new RechteForm.CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        userBinder.addStatusChangeListener(e -> speichern.setEnabled(userBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung des Mitarbeiters.
     * @error Bei misslungener Speicherung (ValidationException) wird der Fehler in der Konsole ausgegeben.
     */
    private void checkAndSave() {
        try {
            System.out.println(selectedUser);
            userBinder.writeBean(selectedUser);
            fireEvent(new RechteForm.SaveEvent(this, selectedUser));
            Notification.show(selectedUser.getName() + " wurde erfolgreich bearbeitet!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class RechteFormEvent extends ComponentEvent<RechteForm>{
        private final User user;

        protected RechteFormEvent(RechteForm source, User user){
            super(source, false);
            this.user = user;
        }

        public User getUser() { return user;}
    }

    /**
     * @desc Event zur Speicherung der Daten eines Mitarbeiters.
     */
    public static class SaveEvent extends RechteFormEvent {
        SaveEvent(RechteForm source, User user) {
            super(source, user);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends RechteFormEvent {
        CloseEvent(RechteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
