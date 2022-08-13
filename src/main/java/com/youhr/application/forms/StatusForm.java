package com.youhr.application.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.youhr.application.data.entity.Antrag;
import com.youhr.application.data.entity.Status;
import org.springframework.transaction.TransactionSystemException;

public class StatusForm extends FormLayout {
    private final Binder<Antrag> antragBinder = new BeanValidationBinder<>(Antrag.class);

    private final H5 titel = new H5("Status des Antrags ändern");

    private final ComboBox<Status> statuses = new ComboBox<>("Status");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Antrag selectedAntrag;

    public void setSelectedAntrag(Antrag selectedAntrag){
        if(selectedAntrag != null){
            this.selectedAntrag = selectedAntrag;
        }
        antragBinder.readBean(selectedAntrag);
    }

    public StatusForm(){
        addClassName("Status-Formular");
        statuses.setMaxWidth("350px");
        setMaxWidth("400px");
        add(titel, statuses, createButtonLayout());
    }

    /**
     * @desc Konfiguration der Ausrichtung der Buttons und deren Event bei Betätigung (Klick).
     */
    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkAndSave());
        schliessen.addClickListener(event -> fireEvent(new StatusForm.CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        antragBinder.addStatusChangeListener(e -> speichern.setEnabled(antragBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung des Users.
     * @error Bei misslungener Speicherung (falsches Passwort oder nicht bestätigt) wird der Nutzer darüber informiert.
     */
    private void checkAndSave() {
        try {
            selectedAntrag.setStatus(statuses.getValue());
            antragBinder.writeBean(selectedAntrag);
            fireEvent(new StatusForm.SaveEvent(this, selectedAntrag));

            Notification.show("Der Status von Antrag " + selectedAntrag.getId() + " wurde zu " + selectedAntrag.getStatus().getName() + " geändert!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (TransactionSystemException | ValidationException e) {
            Notification.show("Ein Fehler ist aufgetreten!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class StatusFormEvent extends ComponentEvent<StatusForm> {
        private final Antrag antrag;

        protected StatusFormEvent(StatusForm source, Antrag antrag) {
            super(source, false);
            this.antrag = antrag;
        }

        public Antrag getAntrag() {
            return antrag;
        }
    }

    /**
     * @desc Event zur Speicherung der Daten eines Mitarbeiters.
     */
    public static class SaveEvent extends StatusFormEvent {
        SaveEvent(StatusForm source, Antrag antrag) {
            super(source, antrag);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends StatusFormEvent {
        CloseEvent(StatusForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
