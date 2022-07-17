package com.example.application.views.einstellungen;

import com.example.application.data.entity.Abteilung;
import com.example.application.data.entity.Team;
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

import java.util.List;
import java.util.Locale;

/**
 * @desc Das Formular Abteilung erstellt eine Eingabemaske für die Erstellung eines Objekts Abteilung.
 *
 * @category Form
 * @version 1.0
 * @since 2022-07-08
 */
public class AbteilungForm extends FormLayout {
    private Binder<Abteilung> abteilungBinder = new BeanValidationBinder<>(Abteilung.class);

    Locale finnishLocale = new Locale("fi", "FI");

    private TextField bezeichnung = new TextField("Bezeichnung");
    
    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Abteilung selectedAbteilung;

    /**
     * @desc Lesen der Abteilungsdaten zum Ausfüllen des Formulars bei Bearbeitung
     * @param selectedAbteilung
     */
    public void setSelectedAbteilung(Abteilung selectedAbteilung) {
        if (selectedAbteilung == null) {
            this.selectedAbteilung = new Abteilung();
            this.selectedAbteilung.setBezeichnung("-");
        } else {
            this.selectedAbteilung = selectedAbteilung;
        }

        abteilungBinder.readBean(selectedAbteilung);
    }

    /**
     * @desc Binden der Eingabefelder an die Attribute des Objekts. Außerdem wird das Formular (Text + Eingabefelder + Buttons) initialisiert.
     * @param abteilungen -> Vorhandene Abteilungen werden aus Datenbank gelesen
     * @param teams -> Vorhandene Teams werden aus Datenbank gelesen
     */
    @Autowired
    public AbteilungForm(List<Abteilung> abteilungen, List<Team> teams) {
        addClassName("Abteilung-Formular");

        abteilungBinder.bindInstanceFields(this);

        setMaxWidth("600px");
        setResponsiveSteps(new ResponsiveStep("0",2));

        add(
                createButtonLayout()
        );
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

        abteilungBinder.addStatusChangeListener(e -> speichern.setEnabled(abteilungBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung der Abteilung.
     * @error Bei misslungener Speicherung (ValidationException) wird der Fehler in der Konsole ausgegeben.
     */
    private void checkAndSave() {
        try {
            selectedAbteilung.setBezeichnung(bezeichnung.getValue());
            System.out.println(selectedAbteilung);

            abteilungBinder.writeBean(selectedAbteilung);
            fireEvent(new SaveEvent(this, selectedAbteilung));
            Notification.show(selectedAbteilung.getBezeichnung() + " wurde erstellt.");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class AbteilungFormEvent extends ComponentEvent<AbteilungForm> {
        private Abteilung abteilung;

        protected AbteilungFormEvent(AbteilungForm source, Abteilung abteilung) {
            super(source, false);
            this.abteilung = abteilung;
        }

        public Abteilung getAbteilung() {
            return abteilung;
        }
    }

    /**
     * @desc Event zur Speicherung der Daten einer Abteilung.
     */
    public static class SaveEvent extends AbteilungFormEvent {
        SaveEvent(AbteilungForm source, Abteilung abteilung) {
            super(source, abteilung);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends AbteilungFormEvent {
        CloseEvent(AbteilungForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
