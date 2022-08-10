package com.youhr.application.forms;

import com.youhr.application.data.entity.Team;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @desc Das Formular Team erstellt eine Eingabemaske für die Erstellung eines Objekts Team.
 *
 * @category Form
 * @version 1.0
 * @since 2022-07-08
 */
public class TeamForm extends FormLayout {
    private final Binder<Team> teamBinder = new BeanValidationBinder<>(Team.class);

    private final TextField bezeichnung = new TextField("Bezeichnung");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Team selectedTeam;

    /**
     * @desc Lesen der Teamdaten zum Ausfüllen des Formulars bei Bearbeitung
     * @param selectedTeam
     */
    public void setSelectedTeam(Team selectedTeam) {
        if (selectedTeam == null) {
            this.selectedTeam = new Team();
            this.selectedTeam.setBezeichnung("-");
        } else {
            this.selectedTeam = selectedTeam;
        }

        teamBinder.readBean(selectedTeam);
    }

    /**
     * @desc Binden der Eingabefelder an die Attribute des Objekts. Außerdem wird das Formular (Text + Eingabefelder + Buttons) initialisiert.
     */
    @Autowired
    public TeamForm() {
        addClassName("Team-Formular");

        teamBinder.bindInstanceFields(this);

        setMaxWidth("600px");

        add(bezeichnung, createButtonLayout());
    }

    /**
     * @desc Konfiguration der Ausrichtung der Buttons und deren Event bei Betätigung (Klick).
     */
    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkAndSave());
        schliessen.addClickListener(event -> fireEvent(new CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        teamBinder.addStatusChangeListener(e -> speichern.setEnabled(teamBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung des Teams.
     * @error Bei misslungener Speicherung (ValidationException) wird der Fehler in der Konsole ausgegeben.
     */
    private void checkAndSave() {
        try {
            selectedTeam.setBezeichnung(bezeichnung.getValue());
            teamBinder.writeBean(selectedTeam);
            fireEvent(new SaveEvent(this, selectedTeam));
            Notification.show(selectedTeam.getBezeichnung() + " " + " wurde erstellt.");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class TeamFormEvent extends ComponentEvent<TeamForm> {
        private final Team team;

        protected TeamFormEvent(TeamForm source, Team team) {
            super(source, false);
            this.team = team;
        }

        public Team getTeam() {
            return team;
        }
    }

    /**
     * @desc Event zur Speicherung der Daten eines Teams.
     */
    public static class SaveEvent extends TeamFormEvent {
        SaveEvent(TeamForm source, Team team) {
            super(source, team);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends TeamFormEvent {
        CloseEvent(TeamForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
