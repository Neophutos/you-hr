package com.youhr.application.views;

import com.youhr.application.data.entity.User;
import com.youhr.application.data.service.UserService;
import com.youhr.application.forms.PasswordForm;
import com.youhr.application.forms.RechteForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.youhr.application.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

/**
 * @desc Der View Rechteverwaltung implementiert eine Tabellenansicht für alle vorhandenen Mitarbeitern und deren
 * entsprechenden Rechte.
 *
 * @category View
 * @author Tim Freund, Ben Köppe, Chris Zobel
 * @version 1.0
 * @since 2022-06-30
 */

@PageTitle("Rechteverwaltung")
@Route(value = "rechteverwaltung/:rechteverwaltungID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class RechteverwaltungView extends Div {

    private final Grid<User> grid = new Grid<>(User.class, false);

    Dialog editDialog = new Dialog();
    Dialog editPasswortDialog = new Dialog();
    Dialog deletionDialog = new Dialog();

    Button cancelButton;
    Button confirmButton;

    RechteForm rechteForm;
    PasswordForm passwordForm;
    private final UserService userService;

    User user;

    /**
     * @desc Initialisierung des grafischen Interfaces und des Menüs bei Rechtsklick auf die Tabelle
     * @param userService
     */
    @Autowired
    public RechteverwaltungView(UserService userService) {
        this.userService = userService;
        addClassName("rechteverwaltung-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureContextMenu();
        add(getContent());
        updateList();

        VerticalLayout editDialogLayout = createEditDialogLayout();
        editDialog.add(editDialogLayout);
        editDialog.setHeaderTitle("Nutzerdaten ändern");

        VerticalLayout editPasswortDialogLayout = createEditPasswortDialogLayout();
        editPasswortDialog.add(editPasswortDialogLayout);
        editPasswortDialog.setHeaderTitle("Passwort ändern");
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
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts.
     */
    private VerticalLayout createEditDialogLayout() {
        VerticalLayout editDialogLayout = new VerticalLayout(rechteForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    /**
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts.
     */
    private VerticalLayout createEditPasswortDialogLayout() {
        VerticalLayout editPasswortDialogLayout = new VerticalLayout(passwordForm);
        editPasswortDialogLayout.setPadding(false);
        editPasswortDialogLayout.setSpacing(false);
        editPasswortDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editPasswortDialogLayout;
    }

    /**
     * @desc Grafische Konfiguration der Tabelle für Userdarstellung.
     */
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    /**
     * @desc Initialisierung der Formulare mit dem entsprechenden Service zur Kommunikation mit der Datenbank.
     * @desc Zuordnung der entsprechenden Listener bei Button-Ausführung
     */
    private void configureForm() {
        rechteForm = new RechteForm();
        rechteForm.addListener(RechteForm.SaveEvent.class, this::saveUser);
        rechteForm.addListener(RechteForm.CloseEvent.class, e -> editDialog.close());

        passwordForm = new PasswordForm();
        passwordForm.addListener(PasswordForm.SaveEvent.class, this::savePasswort);
        passwordForm.addListener(PasswordForm.CloseEvent.class, e -> editPasswortDialog.close());
    }

    private void configureContextMenu(){
        GridContextMenu<User> menu = grid.addContextMenu();

        GridMenuItem<User> bearbeiten = menu.addItem("Bearbeiten", event -> editUser(grid.asSingleSelect().getValue()));
        bearbeiten.addComponentAsFirst(createIcon(VaadinIcon.EDIT));

        GridMenuItem<User> changePasswort = menu.addItem("Passwort ändern", event -> editPasswort(grid.asSingleSelect().getValue()));
        changePasswort.addComponentAsFirst(createIcon(VaadinIcon.PASSWORD));

        GridMenuItem<User> loeschen = menu.addItem("Löschen", event -> removeUser(grid.asSingleSelect().getValue()));
        loeschen.addComponentAsFirst(createIcon(VaadinIcon.ERASER));
    }

    /**
     * @desc Einrichtung der Tabelle -> Setzen der Spalten
     */
    private void configureGrid() {
        grid.addClassName("rechteverwaltung-grid");
        grid.setColumns("id", "name", "username");
        grid.addColumn("roles").setHeader("Berechtigungen");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    /**
     * @desc Speicher-Event für User
     * @param event
     */
    private void saveUser(RechteForm.SaveEvent event) {
        userService.update(event.getUser());
        updateList();
        editDialog.close();
    }

    /**
     * @desc Speicher-Event für User
     * @param event
     */
    private void savePasswort(PasswordForm.SaveEvent event) {
        userService.update(event.getUser());
        updateList();
        editPasswortDialog.close();
    }

    /**
     * @desc Öffnen des Rechteformulars zur Bearbeitungs eines Users
     * @see RechteForm
     * @param user
     */
    private void editUser(User user) {
        if (user == null) {
            Notification.show("Es wurde kein User ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            rechteForm.setSelectedUser(user);
            editDialog.open();
        }
    }

    /**
     * @desc Öffnen des Passwordformulars zur Bearbeitung des Nutzerpassworts
     * @see PasswordForm
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
     * @desc Initialisierung der Oberfläche für Löschen eines Users
     * @param user
     */
    private void removeUser(User user) {
        if (user == null) {
            Notification.show("Es wurde kein User ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deletionDialog = new Dialog();

            deletionDialog.setHeaderTitle("User " + user.getUsername() + " wirklich löschen?");

            cancelButton = createCancelButton(deletionDialog);
            deletionDialog.getFooter().add(cancelButton);
            confirmButton = createConfirmButton(deletionDialog, user);
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
     * @desc Erstellung der Button-Logik bei Bestätigung des Löschens eines Users
     */
    private Button createConfirmButton(Dialog confirmDialog, User user) {
        Button saveButton = new Button("Abschließen", e -> {
            userService.delete(user);
            Notification.show("User " + user.getUsername() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * @desc Update der Tabelle -> Nutzung bei durchgeführten Änderungen in der Tabelle
     */
    private void updateList() { grid.setItems(userService.findAll());
    }
}
