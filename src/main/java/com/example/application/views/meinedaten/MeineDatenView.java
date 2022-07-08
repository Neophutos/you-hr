package com.example.application.views.meinedaten;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.generator.DataGenerator;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

/**
 * Der View MeineDaten stellt die Informationen des aktiven Nutzers
 * in einem Interface dar. Diese können nur angesehen, nicht bearbeitet werden.
 *
 * @author Tim Freund, Ben Köppe
 * @version 1.0
 * @since 2022-07-01
 */
@PageTitle("Meine Daten")
@Route(value = "meine_daten", layout = MainLayout.class)
@RolesAllowed("USER")
public class MeineDatenView extends Div {

    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());
    private MitarbeiterService mitarbeiterService;

    private Binder<Mitarbeiter> mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

    private H5 personal = new H5("Persönliche Informationen");
    private H5 beruf = new H5("Berufliche Informationen");

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private TextField geburtsdatum = new TextField("Geburtsdatum");
    private TextField email = new TextField("Email");
    private TextField telefonnr = new TextField("Telefon");
    private TextField abteilung = new TextField("Abteilung");
    private TextField position = new TextField("Position");
    private TextField adresse = new TextField("Adresse");

    Mitarbeiter mitarbeiter;
    /**
     * Diese Methode initialisiert das grafische Interface
     * und fügt diese zum View hinzu.
     *
     * @param mitarbeiterService
     */
    public MeineDatenView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;

        setMitarbeiterFromUser();

        Stream.of(vorname, nachname, geburtsdatum, email, telefonnr, abteilung, position, adresse).forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });

        add(getContent());
    }

    /**
     * Diese Methode konfiguriert die Ausrichtung
     * und Form der Ansicht.
     */
    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout(getData());
        content.setPadding(true);

        return content;
    }

    /**
     * Diese Methode initialisiert die anzuzeigenden Daten des Nutzers.
     */
    private FormLayout getData(){
        FormLayout dataLayout = new FormLayout();
        dataLayout.add(
                personal,
                vorname,
                nachname,
                geburtsdatum,
                adresse,
                beruf,
                email,
                telefonnr,
                position,
                abteilung);

        dataLayout.setColspan(personal, 2);
        dataLayout.setColspan(beruf, 2);
        dataLayout.setColspan(adresse, 2);


        dataLayout.setMaxWidth("600px");
        dataLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));

        return dataLayout;
    }

    /**
     * Diese Methode liest die Informationen über den derzeitigen Nutzer aus.
     * Anschließend werden die Ausgabefelder mit den gewonnenen Informationen befüllt.
     * Bei einem Fehler bekommt der Nutzer eine Nachricht, das seine Informationen nicht vorhanden sind.
     *
     * Sonderfall:
     * Generische Rollen-Accounts (ohne Verbindung zu Mitarbeiter) können keine eigenen Daten haben.
     */
    public void setMitarbeiterFromUser() {
        if (authenticatedUser.get().get().getMitarbeiter() != null) {
            this.mitarbeiter = authenticatedUser.get().get().getMitarbeiter();
            vorname.setValue(mitarbeiter.getVorname());
            nachname.setValue(mitarbeiter.getNachname());
            geburtsdatum.setValue(mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            email.setValue(mitarbeiter.getEmail());
            telefonnr.setValue(mitarbeiter.getTelefonnr());
            abteilung.setValue(mitarbeiter.getAbteilung().toString());
            position.setValue(mitarbeiter.getPosition());
            adresse.setValue(mitarbeiter.getAdresse().toString());
        } else {
            Notification.show("Mitarbeiter wurde nicht gefunden!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

}
