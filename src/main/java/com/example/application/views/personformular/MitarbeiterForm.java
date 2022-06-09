package com.example.application.views.personformular;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.mitarbeiterliste.MitarbeiterlisteView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MitarbeiterForm extends FormLayout {
    private Binder<Mitarbeiter> mitarbeiterBinder;

    Locale finnishLocale = new Locale("fi","FI");

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private EmailField email = new EmailField("E-Mail");
    private DatePicker geburtsdatum = new DatePicker("Geburtstag");
    private TextField telefonnr = new TextField("Telefonnummer");
    private TextField position = new TextField("Position");
    private ComboBox<String> abteilung = new ComboBox<>("Abteilung");

    private TextField strassenname = new TextField("Strasse");
    private IntegerField hausnummer = new IntegerField ("Hausnummer");
    private IntegerField plz = new IntegerField("Postleitzahl");
    private TextField stadt = new TextField("Stadt");
    private ComboBox<String> bundesland = new ComboBox<>("Bundesland");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Mitarbeiter mitarbeiter;

    private Adresse adresse = new Adresse(1,"",2,"","");

    private MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterForm(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassName("Mitarbeiter-Formular");

        mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

        mitarbeiterBinder.bindInstanceFields(this);

        mitarbeiterBinder.forField(strassenname).bind("adresse.strassenname");
        mitarbeiterBinder.forField(hausnummer).bind("adresse.hausnummer");
        mitarbeiterBinder.forField(bundesland).bind("adresse.bundesland");
        mitarbeiterBinder.forField(stadt).bind("adresse.stadt");
        mitarbeiterBinder.forField(plz).bind("adresse.plz");

        geburtsdatum.setLocale(finnishLocale);

        abteilung.setItems("Buchhaltung","Forschung & Entwicklung","Geschäftsleitung","IT & EDV","Kundendienst", "Marketing", "Personalwesen");
        bundesland.setItems("Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen");

        add(
                new H6("Persönliche Informationen"),
                vorname,
                nachname,
                email,
                geburtsdatum,
                telefonnr,
                position,
                abteilung,
                new H6("Anschrift"),
                strassenname,
                hausnummer,
                plz,
                stadt,
                bundesland,
                createButtonLayout()
        );
    }

    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkUndSpeichern());
        schliessen.addClickListener(event -> schliessen.getUI().ifPresent(ui ->
                ui.navigate("mitarbeiterliste")));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        mitarbeiterBinder.addStatusChangeListener(e -> speichern.setEnabled(mitarbeiterBinder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    private void checkUndSpeichern() {
        try {
            this.mitarbeiter = new Mitarbeiter();
            this.mitarbeiter.setAdresse(adresse);

            mitarbeiterBinder.writeBean(mitarbeiter);

            this.mitarbeiter.generateUser();

            mitarbeiterService.update(mitarbeiter);

            Notification.show(mitarbeiter.getNachname() + " " + mitarbeiter.getVorname() + " wurde erstellt.");

            UI.getCurrent().navigate(MitarbeiterlisteView.class);

        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
