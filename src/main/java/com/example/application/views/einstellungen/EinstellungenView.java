package com.example.application.views.einstellungen;

import com.example.application.data.entity.Abteilung;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Team;
import com.example.application.data.service.GruppenService;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.MainLayout;
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
import javax.annotation.security.RolesAllowed;

/**
 * @desc Im View Einstellungen können sowohl Admins, als auch Personaler Abteilungen und Teams hinzufügen oder entfernen, falls dies nötig ist.
 *
 * @category View
 * @version 0.0
 * @since 2022-00-00
 */
@PageTitle("Einstellungen")
@Route(value = "einstellungen", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EinstellungenView extends VerticalLayout {

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

    public EinstellungenView(GruppenService gruppenService) {
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

    private void configureForms(){
        abteilungForm = new AbteilungForm();
        abteilungForm.addListener(AbteilungForm.SaveEvent.class, this::saveAbteilung);
        abteilungForm.addListener(AbteilungForm.CloseEvent.class, e -> editAbteilungDialog.close());

        teamForm = new TeamForm();
        teamForm.addListener(TeamForm.SaveEvent.class, this::saveTeam);
        teamForm.addListener(TeamForm.CloseEvent.class, e -> editTeamDialog.close());
    }

    private VerticalLayout createEditAbteilungDialogLayout(){
        VerticalLayout editDialogLayout = new VerticalLayout(abteilungForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

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

    public void addAbteilung(){
        abteilungGrid.asSingleSelect().clear();
        abteilung = new Abteilung();
        editAbteilung(abteilung);
    }

    public void editAbteilung(Abteilung abteilung){
        if(abteilung == null) {
            Notification.show("Es wurde keine Abteilung ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            abteilungForm.setSelectedAbteilung(abteilung);
            editAbteilungDialog.open();
        }
    }

    public void addTeam(){
        teamGrid.asSingleSelect().clear();
        team = new Team();
        editTeam(team);
    }

    public void editTeam(Team team){
        if(team == null) {
            Notification.show("Es wurde kein Team ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            teamForm.setSelectedTeam(team);
            editTeamDialog.open();
        }
    }

    private void removeAbteilung(Abteilung abteilung){
        if (abteilung == null){
            Notification.show("Es wurde keine Abteilung ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deleteAbteilungDialog = new Dialog();

            deleteAbteilungDialog.setHeaderTitle("Abteilung " + abteilung + " wirklich löschen?");

            cancelButton = createCancelButton(deleteAbteilungDialog);
            confirmButton = createConfirmButton(deleteAbteilungDialog, abteilung, null);
            deleteAbteilungDialog.getFooter().add(cancelButton, confirmButton);
        }
    }

    private void removeTeam(Team team){
        if (team == null){
            Notification.show("Es wurde kein Team ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deleteTeamDialog = new Dialog();

            deleteTeamDialog.setHeaderTitle("Team " + team + " wirklich löschen?");

            cancelButton = createCancelButton(deleteTeamDialog);
            confirmButton = createConfirmButton(deleteTeamDialog, null, team);
            deleteTeamDialog.getFooter().add(cancelButton, confirmButton);
        }
    }

    private Button createCancelButton(Dialog confirmDialog){
        return new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Vorgang wurde abgebrochen");
            updateLists();
        });
    }

    private Button createConfirmButton(Dialog confirmDialog, Abteilung abteilung, Team team){
        Button saveButton = new Button("Abschließen", e -> {
            if (abteilung != null) {
                gruppenService.deleteAbteilung(abteilung);
            } else if (team != null) {
                gruppenService.deleteTeam(team);
            } else {
                Notification.show("Die angegebene Gruppe wurde nicht gefunden").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            confirmDialog.close();
            updateLists();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private void updateLists(){
        abteilungGrid.setItems(gruppenService.findAllAbteilungen());
        teamGrid.setItems(gruppenService.findAllTeams());
    }
}
