package com.youhr.application.forms;

import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Mitarbeiter;
import com.youhr.application.data.entity.Team;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Locale;

/**
 * @desc Das Formular Mitarbeiter erstellt eine Eingabemaske für die Erstellung eines Objekts Mitarbeiter.
 *
 * @category Form
 * @author Ben Köppe, Tim Freund, Riccardo Prochnow, Chris Zobel
 * @version 1.0
 * @since 2022-07-08
 */
public class MitarbeiterForm extends FormLayout {
    private final Binder<Mitarbeiter> mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

    Locale finnishLocale = new Locale("fi", "FI");

    private final H5 personal = new H5("Persönliche Informationen");
    private final H5 anschrift = new H5("Anschrift");

    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final EmailField email = new EmailField("E-Mail");
    private final DatePicker geburtsdatum = new DatePicker("Geburtstag");
    private final DatePicker.DatePickerI18n germanI18n = new DatePicker.DatePickerI18n();
    private final TextField telefonnr = new TextField("Telefonnummer (Mobil)");
    private final TextField position = new TextField("Position");
    private final ComboBox<Abteilung> abteilungen = new ComboBox<>("Abteilung");
    private final ComboBox<Team> teams = new ComboBox<>("Team");

    private final TextField strassenname = new TextField("Strasse");
    private final IntegerField hausnummer = new IntegerField("Hausnummer");
    private final IntegerField plz = new IntegerField("Postleitzahl");
    private final TextField stadt = new TextField("Stadt");
    private final ComboBox<String> bundesland = new ComboBox<>("Bundesland");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Mitarbeiter selectedMitarbeiter;

    /**
     * @desc Lesen der Mitarbeiterdaten zum Ausfüllen des Formulars bei Bearbeitung
     * @param selectedMitarbeiter
     */
    public void setSelectedMitarbeiter(Mitarbeiter selectedMitarbeiter) {
        if (selectedMitarbeiter == null) {
            this.selectedMitarbeiter = new Mitarbeiter();
            this.selectedMitarbeiter.setAbteilung(new Abteilung());
            this.selectedMitarbeiter.setTeam(new Team());
        } else {
            this.selectedMitarbeiter = selectedMitarbeiter;
        }

        mitarbeiterBinder.readBean(selectedMitarbeiter);
    }

    /**
     * @desc Binden der Eingabefelder an die Attribute des Objekts. Außerdem wird das Formular (Text + Eingabefelder + Buttons) initialisiert.
     * @param abteilungen -> Vorhandene Abteilungen werden aus Datenbank gelesen
     * @param teams -> Vorhandene Teams werden aus Datenbank gelesen
     */
    @Autowired
    public MitarbeiterForm(List<Abteilung> abteilungen, List<Team> teams) {
        addClassName("Mitarbeiter-Formular");

        mitarbeiterBinder.bindInstanceFields(this);
        this.abteilungen.setItems(abteilungen);
        this.abteilungen.setItemLabelGenerator(Abteilung::getBezeichnung);
        this.teams.setItems(teams);
        this.teams.setItemLabelGenerator(Team::getBezeichnung);

        mitarbeiterBinder.forField(strassenname).bind("adresse.strassenname");
        mitarbeiterBinder.forField(hausnummer).bind("adresse.hausnummer");
        mitarbeiterBinder.forField(bundesland).bind("adresse.bundesland");
        mitarbeiterBinder.forField(stadt).bind("adresse.stadt");
        mitarbeiterBinder.forField(plz).bind("adresse.plz");


        germanI18n.setMonthNames(List.of("Januar", "Februar", "März", "April", "Mai",
                "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"));
        germanI18n.setWeekdays(List.of("Sonntag", "Montag", "Dienstag", "Mittwoch",
                "Donnerstag", "Freitag", "Samstag"));
        germanI18n.setWeekdaysShort(List.of("So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"));
        germanI18n.setWeek("Woche");
        germanI18n.setToday("Heute");
        germanI18n.setCancel("Abbrechen");
        geburtsdatum.setI18n(germanI18n);

        email.getElement().setAttribute("name","email");

        this.abteilungen.setAllowCustomValue(false);
        this.abteilungen.setRequired(true);
        this.abteilungen.setPlaceholder("Abteilung wählen");
        this.teams.setAllowCustomValue(false);
        this.teams.setRequired(true);
        this.teams.setPlaceholder("Team wählen");

        geburtsdatum.setLocale(finnishLocale);

        telefonnr.setHelperText("Bsp.: +49 472 18324421");

        bundesland.setItems("Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen");

        setColspan(personal, 2);
        setColspan(anschrift, 2);

        setMaxWidth("600px");
        setResponsiveSteps(new ResponsiveStep("0",2));

        add(
                personal,
                vorname,
                nachname,
                email,
                geburtsdatum,
                telefonnr,
                position,
                this.abteilungen,
                this.teams,
                anschrift,
                strassenname,
                hausnummer,
                plz,
                stadt,
                bundesland,
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

        mitarbeiterBinder.addStatusChangeListener(e -> speichern.setEnabled(mitarbeiterBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    /**
     * @desc Diese Methode speichert bei Aufruf die Änderungen bzw. Erstellung des Mitarbeiters.
     * @error Bei misslungener Speicherung (ValidationException) wird der Fehler in der Konsole ausgegeben.
     */
    private void checkAndSave() {
        try {
            selectedMitarbeiter.setAbteilung(abteilungen.getValue());
            selectedMitarbeiter.setTeam(teams.getValue());
            mitarbeiterBinder.writeBean(selectedMitarbeiter);
            fireEvent(new SaveEvent(this, selectedMitarbeiter));

            Notification.show(selectedMitarbeiter.getNachname() + ", " + selectedMitarbeiter.getVorname() + " wurde bearbeitet/erstellt").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (TransactionSystemException | ValidationException e) {
            Notification.show("Ein Fehler ist aufgetreten! Haben Sie eine Abteilung und ein Team ausgewählt?").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * @desc Konfiguration der verschiedenen Aktionen (Events), die der Nutzer auslösen kann.
     */
    public static abstract class MitarbeiterFormEvent extends ComponentEvent<MitarbeiterForm> {
        private final Mitarbeiter mitarbeiter;

        protected MitarbeiterFormEvent(MitarbeiterForm source, Mitarbeiter mitarbeiter) {
            super(source, false);
            this.mitarbeiter = mitarbeiter;
        }

        public Mitarbeiter getMitarbeiter() {
            return mitarbeiter;
        }
    }

    /**
     * @desc Event zur Speicherung der Daten eines Mitarbeiters.
     */
    public static class SaveEvent extends MitarbeiterFormEvent {
        SaveEvent(MitarbeiterForm source, Mitarbeiter mitarbeiter) {
            super(source, mitarbeiter);
        }
    }

    /**
     * @desc Event zum Schließen des Formulars.
     */
    public static class CloseEvent extends MitarbeiterFormEvent {
        CloseEvent(MitarbeiterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
