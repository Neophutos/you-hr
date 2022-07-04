package com.example.application.views.privat;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.generator.DataGenerator;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

@PageTitle("Meine Daten")
@Route(value = "meine_daten", layout = MainLayout.class)
@RolesAllowed("USER")
public class PrivatView extends HorizontalLayout {

    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());
    private MitarbeiterService mitarbeiterService;

    private Binder<Mitarbeiter> mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

    private TextField vorname;
    private TextField nachname;
    private TextField geburtsdatum;
    private TextField email;
    private TextField telefonnr;
    private TextField abteilung;
    private TextField position;
    private TextField adresse;

    Mitarbeiter mitarbeiter;

    public PrivatView(MitarbeiterService mitarbeiterService) {
        vorname = new TextField("Vorname");
        nachname = new TextField("Nachname");
        geburtsdatum = new TextField("Geburtsdatum");
        email = new TextField("Email");
        telefonnr = new TextField("Telefon");
        abteilung = new TextField("Abteilung");
        position = new TextField("Position");
        adresse = new TextField("Adresse");

        this.mitarbeiterService = mitarbeiterService;

        setMitarbeiterFromUser();

        Stream.of(vorname, nachname, geburtsdatum, email, telefonnr, abteilung, position, adresse).forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });

        add(new H6("Meine Daten"),
                vorname,
                nachname,
                geburtsdatum,
                email,
                telefonnr,
                abteilung,
                position,
                adresse
        );
    }

    public void setMitarbeiterFromUser() {
        if (authenticatedUser.get().get().getMitarbeiter() != null) {
            this.mitarbeiter = authenticatedUser.get().get().getMitarbeiter();
            vorname.setValue(mitarbeiter.getVorname());
            nachname.setValue(mitarbeiter.getNachname());
            geburtsdatum.setValue(mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            email.setValue(mitarbeiter.getEmail());
            telefonnr.setValue(mitarbeiter.getTelefonnr());
            abteilung.setValue(mitarbeiter.getAbteilung());
            position.setValue(mitarbeiter.getPosition());
            adresse.setValue(mitarbeiter.getAdresse().toString());
        } else {
            System.out.println("Mitarbeiter is null!");
        }
    }

}
