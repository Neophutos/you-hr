package com.example.application.views.rechteverwaltung;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.views.MainLayout;
import com.example.application.views.mitarbeiterliste.MitarbeiterForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

/**
 * @desc Der View Rechteverwaltung implementiert eine Tabellenansicht für alle vorhandenen Mitarbeitern und deren
 * entsprechenden Rechte.
 *
 * @category View
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
    Dialog deletionDialog = new Dialog();

    Button cancelButton;
    Button confirmButton;

    com.example.application.views.rechteverwaltung.RechteForm form;
    private final UserService userService;

    User user;

    @Autowired
    public RechteverwaltungView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();

        addClassNames("rechteverwaltung-view");

        // Configure Grid
        grid.setColumns("id", "name", "username");
        grid.addColumn("roles").setHeader("Berechtigungen");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
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
        form = new com.example.application.views.rechteverwaltung.RechteForm();
        form.addListener(MitarbeiterForm.SaveEvent.class, this::saveUser);
        form.addListener(MitarbeiterForm.CloseEvent.class, e -> editDialog.close());
    }

    private

    private void updateList() { grid.setItems(userService.findAll());
    }
}
