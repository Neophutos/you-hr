package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

/**
 * @desc Der View Mitarbeiterliste stellt alle Mitarbeiter in der Datenbank dar.
 * @desc Mitarbeiterformular wird eingebettet -> Erstellung, Bearbeitung, Löschen per Button bzw. Rechtsklick
 * @desc Realisierung der Detailansicht des ausgewählten Mitarbeiters.
 *
 * @see MitarbeiterForm
 *
 * @category View
 * @version 1.0
 * @since 2022-07-08
 */
@PageTitle("Mitarbeiterliste")
@Route(value = "mitarbeiterliste", layout = MainLayout.class)
@RolesAllowed({"PERSONALER","ADMIN"})
@JsModule("./modules/copytoclipboard.js")
public class MitarbeiterlisteView extends Div {

    Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);
    TextField filterText = new TextField();
    Dialog editDialog = new Dialog();
    Dialog deletionDialog;

    Button cancelButton;
    Button confirmButton;

    MitarbeiterForm form;
    MitarbeiterService mitarbeiterService;

    Mitarbeiter mitarbeiter;

    /**
     * @desc Initialisierung des grafischen Interfaces und des Menüs bei Rechtsklick auf die Tabelle
     * @param mitarbeiterService
     */
    @Autowired
    public MitarbeiterlisteView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassName("mitarbeiterliste-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();

        VerticalLayout editDialogLayout = createEditDialogLayout();
        editDialog.add(editDialogLayout);

        GridContextMenu<Mitarbeiter> menu = grid.addContextMenu();
        GridMenuItem<Mitarbeiter> mail = menu.addItem("E-Mail", event -> getMail(grid.asSingleSelect().getValue()));
        mail.addComponentAsFirst(createIcon(VaadinIcon.ENVELOPES_O));
        menu.add(new Hr());
        GridMenuItem<Mitarbeiter> bearbeiten = menu.addItem("Bearbeiten", event -> editMitarbeiter(grid.asSingleSelect().getValue()));
        bearbeiten.addComponentAsFirst(createIcon(VaadinIcon.EDIT));
        GridMenuItem<Mitarbeiter> loeschen = menu.addItem("Löschen", event -> removeMitarbeiter(grid.asSingleSelect().getValue()));
        loeschen.addComponentAsFirst(createIcon(VaadinIcon.ERASER));

    }

    /**
     * @desc Grafische Konfiguration der Icons im Rechtsklick-Menü
     * @param vaadinIcon
     */
    private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

    /**
     * @desc Ausführung eines JS-Commands zur Speicherung der ausgewählten Mail in die Zwischenablage
     * @param mitarbeiter
     */
    private void getMail(Mitarbeiter mitarbeiter){
        UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", mitarbeiter.getEmail());
        Notification.show("Die E-Mail von " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname() + " wurde kopiert!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts.
     */
    private VerticalLayout createEditDialogLayout() {
        VerticalLayout editDialogLayout = new VerticalLayout(form);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    /**
     * @desc Grafische Konfiguration der Tabelle für Mitarbeiterdarstellung.
     */
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    /**
     * @desc Initialisierung des Formulars mit dem entsprechenden Service zur Kommunikation mit der Datenbank.
     * @desc Zuordnung der entsprechenden Listener bei Button-Ausführung
     */
    private void configureForm() {
        form = new MitarbeiterForm(mitarbeiterService.findAllAbteilungen(), mitarbeiterService.findAllTeams());
        form.addListener(MitarbeiterForm.SaveEvent.class, this::saveMitarbeiter);
        form.addListener(MitarbeiterForm.CloseEvent.class, e -> editDialog.close());
    }

    /**
     * @desc Speicher-Event für Mitarbeiter -> Aufruf der Methode generateUser()
     * @param event
     */
    private void saveMitarbeiter(MitarbeiterForm.SaveEvent event) {
        event.getMitarbeiter().generateUser();
        mitarbeiterService.update(event.getMitarbeiter());
        updateList();
        editDialog.close();
    }

    /**
     * @desc Initialisierung der Leiste über der Tabelle (Suchleiste und Erstellungs-Button)
     */
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Nach Name filtern...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("300px");

        Button addMitarbeiter = new Button("Hinzufügen");

        addMitarbeiter.addClickListener(e -> addMitarbeiter());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addMitarbeiter);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    /**
     * @desc Aufruf der Methode editMitarbeiter() mit einem leeren (neuen) Mitarbeiter
     */
    private void addMitarbeiter() {
        grid.asSingleSelect().clear();
        mitarbeiter = new Mitarbeiter();
        Adresse adresse = new Adresse();
        mitarbeiter.setAdresse(adresse);
        editMitarbeiter(mitarbeiter);
    }

    /**
     * @desc Öffnen des Mitarbeiterformulars zur Bearbeitungs (oder Erstellung) eines Mitarbeiters
     * @see MitarbeiterForm
     * @param mitarbeiter
     */
    private void editMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null) {
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            form.setSelectedMitarbeiter(mitarbeiter);
            editDialog.open();
        }
    }

    /**
     * @desc Einrichtung der Tabelle -> Setzen der Spalten und hinzufügen der Detailansicht
     */
    private void configureGrid() {
        grid.addClassNames("mitarbeiter-grid");
        grid.setSizeFull();
        grid.setColumns("vorname", "nachname", "email");
        grid.addColumn(mitarbeiter -> mitarbeiter.getAbteilung().getBezeichnung()).setHeader("Abteilung");

        grid.setItemDetailsRenderer(createMitarbeiterDetailsRenderer());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private static ComponentRenderer<MitarbeiterDetailsFormLayout, Mitarbeiter> createMitarbeiterDetailsRenderer() {
        return new ComponentRenderer<>(MitarbeiterDetailsFormLayout::new,
                MitarbeiterDetailsFormLayout::setMitarbeiter);
    }

    /**
     * @desc Initialisierung der Detailansicht für Mitarbeitertabelle
     */
    private static class MitarbeiterDetailsFormLayout extends FormLayout {
        private H6 personal = new H6("Persönliche Angaben");
        private H6 beruf = new H6("Berufliche Angaben");
        private H6 kontakt = new H6("Kontakt");

        private final TextField name = new TextField("Name");
        private final TextField geburtsdatum = new TextField("Geburtsdatum");
        private final TextField email = new TextField("Email");
        private final TextField telefonnr = new TextField("Telefon");
        private final TextField position = new TextField("Position");
        private final TextField team = new TextField("Team");
        private final TextField abteilung = new TextField("Abteilung");
        private final TextField adresse = new TextField("Anschrift");

        /**
         * @desc Hinzufügen der Ausgabefelder zur grafischen Oberfläche
         */
        public MitarbeiterDetailsFormLayout() {
            Stream.of(name, geburtsdatum, email, telefonnr, adresse, position, team, abteilung).forEach(field -> {
                field.setReadOnly(true);
            });

            add(
                personal,
                name,
                geburtsdatum,
                beruf,
                position,
                team,
                abteilung,
                kontakt,
                telefonnr,
                email,
                adresse
            );

            setColspan(personal, 2);
            setColspan(beruf, 2);
            setColspan(kontakt, 2);
            setColspan(position, 2);
            setColspan(adresse, 2);
            setMaxWidth("600px");
            setResponsiveSteps(new ResponsiveStep("0", 2));
        }

        /**
         * @desc Setzen der vorhandenen Mitarbeiterdaten in die Ausgabefelder der Detailansicht
         * @param mitarbeiter
         */
        public void setMitarbeiter(Mitarbeiter mitarbeiter) {
            name.setValue(mitarbeiter.getVorname() + " " + mitarbeiter.getNachname());
            geburtsdatum.setValue(mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            email.setValue(mitarbeiter.getEmail());
            telefonnr.setValue(mitarbeiter.getTelefonnr());
            position.setValue(mitarbeiter.getPosition());
            abteilung.setValue(mitarbeiter.getAbteilung().getBezeichnung());
            team.setValue(mitarbeiter.getTeam().getBezeichnung());
            adresse.setValue(mitarbeiter.getAdresse().toString());
        }
    }

    /**
     * @desc Initialisierung der Oberfläche für Löschen eines Mitarbeiters
     * @param mitarbeiter
     */
    private void removeMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null) {
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deletionDialog = new Dialog();

            deletionDialog.setHeaderTitle("Mitarbeiter wirklich löschen?");

            cancelButton = createCancelButton(deletionDialog);
            deletionDialog.getFooter().add(cancelButton);
            confirmButton = createConfirmButton(deletionDialog, mitarbeiter);
            deletionDialog.getFooter().add(confirmButton);

            deletionDialog.open();
        }
    }

    /**
     * @desc Erstellen der Button-Logik bei Abbruch im Löschvorgang
     */
    private Button createCancelButton(Dialog confirmDialog) {
        return new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Vorgang wurde abgebrochen");
            updateList();
        });
    }

    /**
     * @desc Erstellung der Button-Logik bei Bestätigung des Löschens eines Mitarbeiters
     */
    private Button createConfirmButton(Dialog confirmDialog, Mitarbeiter mitarbeiter) {
        Button saveButton = new Button("Abschließen", e -> {
            mitarbeiterService.delete(mitarbeiter);
            Notification.show("Mitarbeiter " + mitarbeiter.getNachname() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * @desc Update der Tabelle -> Nutzung bei durchgeführten Änderungen in der Tabelle
     */
    private void updateList() {
        grid.setItems(mitarbeiterService.findAllByString(filterText.getValue()));
    }
}
