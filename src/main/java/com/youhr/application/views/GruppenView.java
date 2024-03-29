package com.youhr.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Team;
import com.youhr.application.data.service.GruppenService;
import com.youhr.application.forms.AbteilungForm;
import com.youhr.application.forms.TeamForm;
import com.youhr.application.layout.MainLayout;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.security.RolesAllowed;

/**
 * @desc Im View Gruppen können sowohl Admins, als auch Personaler Abteilungen und Teams hinzufügen oder entfernen, falls dies nötig ist.
 *
 * @category View
 * @version 1.0
 * @since 2022-08-03
 */
@PageTitle("Gruppen | YOU-HR")
@Route(value = "gruppen", layout = MainLayout.class)
@RolesAllowed({"ADMIN","PERSONALER"})
public class GruppenView extends VerticalLayout {

    private final Grid<Abteilung> abteilungGrid = new Grid<>(Abteilung.class);
    private final Grid<Team> teamGrid = new Grid<>(Team.class);

    Button cancelButton;
    Button confirmButton;

    Dialog editAbteilungDialog = new Dialog();
    Dialog editTeamDialog = new Dialog();

    Dialog deleteAbteilungDialog = new Dialog();
    Dialog deleteTeamDialog = new Dialog();

    AbteilungForm abteilungForm;
    TeamForm teamForm;

    Abteilung abteilung;
    Team team;

    GruppenService gruppenService;

    /**
     * @desc Initialisierung des grafischen Interfaces und des Menüs bei Rechtsklick auf die Tabelle
     * @param gruppenService
     */
    public GruppenView(GruppenService gruppenService) {
        this.gruppenService = gruppenService;

        addClassName("settings-view");
        setSizeFull();
        configureGrids();
        configureForms();
        add(getContent());
        updateLists();

        VerticalLayout editAbteilungDialogLayout = createEditAbteilungDialogLayout();
        editAbteilungDialog.add(editAbteilungDialogLayout);

        VerticalLayout editTeamDialogLayout = createEditTeamDialogLayout();
        editTeamDialog.add(editTeamDialogLayout);

        GridContextMenu<Abteilung> abteilungMenu = abteilungGrid.addContextMenu();
        GridMenuItem<Abteilung> abteilungErstellen = abteilungMenu.addItem("Erstellen", event -> addAbteilung());
        GridMenuItem<Abteilung> abteilungUmbenennen = abteilungMenu.addItem("Umbenennen", event -> editAbteilung(abteilungGrid.asSingleSelect().getValue()));
        GridMenuItem<Abteilung> abteilungLoeschen = abteilungMenu.addItem("Löschen", event -> removeAbteilung(abteilungGrid.asSingleSelect().getValue()));
        abteilungErstellen.addComponentAsFirst(createIcon(VaadinIcon.FOLDER_ADD));
        abteilungUmbenennen.addComponentAsFirst(createIcon(VaadinIcon.EDIT));
        abteilungLoeschen.addComponentAsFirst(createIcon(VaadinIcon.ERASER));

        GridContextMenu<Team> teamMenu = teamGrid.addContextMenu();
        GridMenuItem<Team> TeamErstellen = teamMenu.addItem("Erstellen", event -> addTeam());
        GridMenuItem<Team> TeamUmbenennen = teamMenu.addItem("Umbenennen", event -> editTeam(teamGrid.asSingleSelect().getValue()));
        GridMenuItem<Team> TeamLoeschen = teamMenu.addItem("Löschen", event -> removeTeam(teamGrid.asSingleSelect().getValue()));
        TeamErstellen.addComponentAsFirst(createIcon(VaadinIcon.FOLDER_ADD));
        TeamUmbenennen.addComponentAsFirst(createIcon(VaadinIcon.EDIT));
        TeamLoeschen.addComponentAsFirst(createIcon(VaadinIcon.ERASER));
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(abteilungGrid, teamGrid);
        content.setFlexGrow(1, abteilungGrid);
        content.setFlexGrow(1, teamGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrids(){
        abteilungGrid.setColumns();
        teamGrid.setColumns();
        abteilungGrid.addColumn(Abteilung::getBezeichnung).setHeader("Abteilungs-Name");
        teamGrid.addColumn(Team::getBezeichnung).setHeader("Team-Name");

        // Ergänzung des Tabellenverhaltens bei Rechtsklick
    }

    /**
     * @desc Initialisierung der Formulare mit dem entsprechenden Service zur Kommunikation mit der Datenbank.
     * @desc Zuordnung der entsprechenden Listener bei Button-Ausführung
     */
    private void configureForms(){
        abteilungForm = new AbteilungForm();
        abteilungForm.addListener(AbteilungForm.SaveEvent.class, this::saveAbteilung);
        abteilungForm.addListener(AbteilungForm.CloseEvent.class, e -> editAbteilungDialog.close());

        teamForm = new TeamForm();
        teamForm.addListener(TeamForm.SaveEvent.class, this::saveTeam);
        teamForm.addListener(TeamForm.CloseEvent.class, e -> editTeamDialog.close());
    }

    /**
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts für Abteilungen.
     */
    private VerticalLayout createEditAbteilungDialogLayout(){
        VerticalLayout editDialogLayout = new VerticalLayout(abteilungForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    /**
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts für Teams.
     */
    private VerticalLayout createEditTeamDialogLayout(){
        VerticalLayout editDialogLayout = new VerticalLayout(teamForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
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
     * @desc Aufruf der Methode editAbteilung() mit einer leeren (neuen) Abteilung.
     */
    public void addAbteilung(){
        abteilungGrid.asSingleSelect().clear();
        abteilung = new Abteilung();
        editAbteilung(abteilung);
    }

    /**
     * @desc Öffnen des Abteilungformulars zur Bearbeitungs (oder Erstellung) einer Abteilung
     * @see AbteilungForm
     * @param abteilung
     */
    public void editAbteilung(Abteilung abteilung){
        if(abteilung == null) {
            Notification.show("Es wurde keine Abteilung ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            abteilungForm.setSelectedAbteilung(abteilung);
            editAbteilungDialog.open();
        }
    }

    /**
     * @desc Aufruf der Methode editTeam() mit einem leeren (neuen) Team.
     */
    public void addTeam(){
        teamGrid.asSingleSelect().clear();
        team = new Team();
        editTeam(team);
    }

    /**
     * @desc Öffnen des Teamformulars zur Bearbeitungs (oder Erstellung) eines Teams
     * @see TeamForm
     * @param team
     */
    public void editTeam(Team team){
        if(team == null) {
            Notification.show("Es wurde kein Team ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            teamForm.setSelectedTeam(team);
            editTeamDialog.open();
        }
    }

    /**
     * @desc Speicher-Event für Abteilungen
     * @param event
     */
    private void saveAbteilung(AbteilungForm.SaveEvent event){
        gruppenService.updateAbteilung(event.getAbteilung());
        updateLists();
        editAbteilungDialog.close();
    }

    /**
     * @desc Speicher-Event für Teams
     * @param event
     */
    private void saveTeam(TeamForm.SaveEvent event){
        gruppenService.updateTeam(event.getTeam());
        updateLists();
        editTeamDialog.close();
    }

    /**
     * @desc Initialisierung der Oberfläche für Löschen einer Abteilung
     * @param abteilung
     */
    private void removeAbteilung(Abteilung abteilung){
        if (abteilung == null){
            Notification.show("Es wurde keine Abteilung ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deleteAbteilungDialog = new Dialog();
            deleteAbteilungDialog.setHeaderTitle("Abteilung " + abteilung.getBezeichnung() + " wirklich löschen?");

            cancelButton = createCancelButton(deleteAbteilungDialog);
            confirmButton = createConfirmAbteilungButton(deleteAbteilungDialog, abteilung);
            deleteAbteilungDialog.getFooter().add(cancelButton, confirmButton);

            deleteAbteilungDialog.open();
        }
    }

    /**
     * @desc Initialisierung der Oberfläche für Löschen eines Teams
     * @param team
     */
    private void removeTeam(Team team){
        if (team == null){
            Notification.show("Es wurde kein Team ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deleteTeamDialog = new Dialog();

            deleteTeamDialog.setHeaderTitle("Team " + team.getBezeichnung() + " wirklich löschen?");

            cancelButton = createCancelButton(deleteTeamDialog);
            confirmButton = createConfirmTeamButton(deleteTeamDialog, team);
            deleteTeamDialog.getFooter().add(cancelButton, confirmButton);

            deleteTeamDialog.open();
        }
    }

    /**
     * @desc Erstellen der Button-Logik bei Abbruch im Löschvorgang
     */
    private Button createCancelButton(Dialog confirmDialog){
        return new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Vorgang wurde abgebrochen");
            updateLists();
        });
    }

    /**
     * @desc Erstellung der Button-Logik bei Bestätigung des Löschens einer Abteilung
     */
    private Button createConfirmAbteilungButton(Dialog confirmDialog, Abteilung abteilung){
        Button saveButton = new Button("Abschließen", e -> {
            try {
                gruppenService.deleteAbteilung(abteilung);
            } catch (DataIntegrityViolationException dataIntegrityViolationException){
                Notification.show("Es sind noch Mitarbeiter vorhanden, die noch dieser Abteilung zugewiesen sind!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            Notification.show("Abteilung " + abteilung.getBezeichnung() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateLists();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * @desc Erstellung der Button-Logik bei Bestätigung des Löschens eines Teams
     */
    private Button createConfirmTeamButton(Dialog confirmDialog, Team team){
        Button saveButton = new Button("Abschließen", e -> {
            try {
                gruppenService.deleteTeam(team);
            } catch (DataIntegrityViolationException dataIntegrityViolationException){
                Notification.show("Es sind noch Mitarbeiter vorhanden, die noch diesem Team zugewiesen sind!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            Notification.show("Team " + team.getBezeichnung() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateLists();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * @desc Update der Tabellen -> Nutzung bei durchgeführten Änderungen in der Tabelle
     */
    private void updateLists(){
        abteilungGrid.setItems(gruppenService.findAllAbteilungen());
        teamGrid.setItems(gruppenService.findAllTeams());
    }
}
