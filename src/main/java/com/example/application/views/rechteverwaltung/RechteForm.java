package com.example.application.views.rechteverwaltung;

import com.example.application.data.Role;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.User;
import com.example.application.views.mitarbeiterliste.MitarbeiterForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;

public class RechteForm extends FormLayout {
    private final Binder<User> userBinder = new Binder();

    private final H5 title = new H5("Berechtigungen bearbeiten");

    private final TextField username = new TextField("Username");
    private final CheckboxGroup rechteCheck = new CheckboxGroup("Berechtigungen", DataProvider.ofItems(Role.values()));

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

        rechteCheck.setItems("Mitarbeiter","Personaler","Admin");
        rechteCheck.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        userBinder.forField(username)
                .asRequired("Sie müssen einen Username angeben")
                .bind("username");
        userBinder.forField(rechteCheck)
                .asRequired("Wählen Sie mind. eine Berechtigung aus")
                .bind(User::getRoles, User::setRoles);

        setMaxWidth("400px");

        add(title, username, rechteCheck);
    }

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
        CloseEvent(MitarbeiterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
