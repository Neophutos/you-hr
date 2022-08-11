package com.youhr.application.views;

import com.youhr.application.data.entity.Mitarbeiter;
import com.youhr.application.data.entity.User;
import com.youhr.application.data.generator.DataGenerator;
import com.youhr.application.data.service.MitarbeiterService;
import com.youhr.application.data.service.UserService;
import com.youhr.application.layout.MainLayout;
import com.youhr.application.security.AuthenticatedUser;
import com.youhr.application.forms.PasswordForm;
import com.youhr.application.forms.RechteForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

/**
 * @desc Der View MeineDaten stellt die Informationen des aktiven Nutzers in einem Interface dar. Diese können nur angesehen, nicht bearbeitet werden.
 *
 * @category View
 * @author Riccardo Prochnow, Chris Zobel, Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-01
 */
@PageTitle("Meine Daten")
@Route(value = "meine_daten", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
public class MeineDatenView extends Div {

    private final AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());
    private final MitarbeiterService mitarbeiterService;

    private final Binder<Mitarbeiter> mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

    private final Dialog editPasswortDialog = new Dialog();

    private final H5 personal = new H5("Persönliche Informationen");
    private final H5 beruf = new H5("Berufliche Informationen");

    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final TextField geburtsdatum = new TextField("Geburtsdatum");
    private final TextField email = new TextField("Email");
    private final TextField telefonnr = new TextField("Telefon");
    private final TextField abteilung = new TextField("Abteilung");
    private final TextField position = new TextField("Position");
    private final TextField adresse = new TextField("Adresse");

    PasswordForm passwordForm;
    private final UserService userService;

    Mitarbeiter mitarbeiter;

    /**
     * @desc Diese Methode initialisiert das grafische Interface und fügt diese zum View hinzu.
     * @param mitarbeiterService
     * @param userService
     */
    public MeineDatenView(MitarbeiterService mitarbeiterService, UserService userService) {
        this.mitarbeiterService = mitarbeiterService;
        this.userService = userService;

        setMitarbeiterFromUser();
        configureForm();
        add(getToolbar());
        VerticalLayout editDialogLayout = createEditDialogLayout();
        editPasswortDialog.add(editDialogLayout);
        editPasswortDialog.setHeaderTitle("Passwort ändern");

        Stream.of(vorname, nachname, geburtsdatum, email, telefonnr, abteilung, position, adresse).forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });

        add(getContent());
    }

    /**
     * @desc Erstellung des Bearbeitungslayouts.
     */
    private VerticalLayout createEditDialogLayout() {
        VerticalLayout editDialogLayout = new VerticalLayout(passwordForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    private void configureForm() {
        passwordForm = new PasswordForm();
        passwordForm.addListener(PasswordForm.SaveEvent.class, this::savePasswort);
        passwordForm.addListener(PasswordForm.CloseEvent.class, e -> editPasswortDialog.close());
    }

    /**
     * @desc Öffnen des Passwortformulars zur Bearbeitung des eigenen Passworts
     * @see RechteForm
     * @param user
     */
    private void editPasswort(User user){
        if (user == null){
            Notification.show("Es wurde kein User ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            passwordForm.setSelectedUser(user);
            editPasswortDialog.open();
        }
    }

    /**
     * @desc Speicher-Event für User
     * @param event
     */
    private void savePasswort(PasswordForm.SaveEvent event) {
        userService.update(event.getUser());
        editPasswortDialog.close();
    }

    /**
     * @desc Konfiguration der Ausrichtung und Form der Ansicht.
     */
    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout(getData());
        content.setPadding(true);

        return content;
    }

    /**
     * @desc Initialisierung der Leiste über der Tabelle (Passwort ändern & Antrag stellen)
     */
    private HorizontalLayout getToolbar() {

        Button changePasswort = new Button("Passwort ändern");
        changePasswort.addClickListener(e -> editPasswort(authenticatedUser.get().get()));

        Button addAntrag = new Button("Antrag stellen");
        addAntrag.addClickListener(e -> addAntrag.getUI().ifPresent(ui -> ui.navigate(
                AntragView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(changePasswort);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    /**
     * @desc Initialisierung der anzuzeigenden Daten des Nutzers.
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
     * @desc Auslesen der Nutzerdaten -> Befüllen der Ausgabefelder mit Informationen von Datenbank
     * @error Nutzer eine Nachricht, das seine Informationen nicht vorhanden sind.
     * @special-case Generische Rollen-Accounts (ohne Verbindung zu Mitarbeiter) können keine eigenen Daten haben.
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
