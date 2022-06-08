package com.example.application.views.personformular;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.HRService;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.mitarbeiterliste.MitarbeiterlisteView;
import com.example.application.views.rechteverwaltung.RechteverwaltungView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
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
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class MitarbeiterForm extends FormLayout {
    private Binder<Mitarbeiter> mitarbeiterBinder;
    private Binder<Adresse> adresseBinder;

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private EmailField email = new EmailField("E-Mail");
    private TextField geburtsdatum = new TextField("Geburtstag");
    private TextField telefonnr = new TextField("Telefonnummer");
    private TextField position = new TextField("Position");
    private ComboBox<String> abteilung = new ComboBox<>("Abteilung");

    private TextField strassenname = new TextField("Strasse");
    private IntegerField hausnummer = new IntegerField ("Hausnummer");
    private IntegerField plz = new IntegerField("Postleitzahl");
    private TextField stadt = new TextField("Stadt");
    private ComboBox<String> Bundesland = new ComboBox<>("Bundesland");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Mitarbeiter mitarbeiter;

    private Adresse adresse;

    private final MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterForm(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassName("Mitarbeiter-Formular");

        mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);
        adresseBinder = new BeanValidationBinder<>(Adresse.class);

        mitarbeiterBinder.bind(hausnummer,
                mitarbeiter -> mitarbeiter.getAdresse().getHausnummer(),
                (mitarbeiter, hausnummer) -> mitarbeiter.getAdresse().setHausnummer(hausnummer));


        abteilung.setItems("Buchhaltung","Forschung & Entwicklung","Geschäftsleitung","IT & EDV","Kundendienst", "Marketing", "Personalwesen");

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
                Bundesland,
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
            if (this.mitarbeiter == null) {
                this.mitarbeiter = new Mitarbeiter();
                this.adresse = new Adresse();
            }
            mitarbeiterBinder.writeBean(mitarbeiter);
            adresseBinder.writeBean(adresse);
            this.mitarbeiter.setAdresse(this.adresse);
            mitarbeiterService.update(mitarbeiter);

            Notification.show("Mitarbeiter details stored.");

            UI.getCurrent().navigate(MitarbeiterlisteView.class);

        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
