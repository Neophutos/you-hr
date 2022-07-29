package com.example.application.views.rechteverwaltung;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.User;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.data.service.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

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

    private final String RECHTEVERWALTUNG_ID = "rechteverwaltungID";
    private final String RECHTEVERWALTUNG_EDIT_ROUTE_TEMPLATE = "rechteverwaltung/%s/edit";

    private final Grid<User> grid = new Grid<>(User.class, false);

    private Checkbox mitarbeiterCheckbox;
    private Checkbox personalerCheckbox;
    private Checkbox adminCheckbox;
    private Checkbox loeschen;
    private Checkbox admin;

    private Button cancelButton = new Button("Abbrechen");
    private Button saveButton = new Button("Speichern");

    private BeanValidationBinder<Mitarbeiter> mitarbeiterBinder;

    private Mitarbeiter mitarbeiter;

    private User user;

    private final UserService userService;


    @Autowired
    public RechteverwaltungView(UserService userService) {
        this.userService = userService;

        addClassNames("rechteverwaltung-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.setColumns("id", "name", "username");
        grid.addColumn("roles").setHeader("Berechtigungen");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        refreshGrid();
    }
    /**
     * @desc  Konfiguration der Ausrichtung & Form der Tabelle, Initialisierung der Interaktionselemente
     * @param splitLayout
     */

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() { grid.setItems(userService.findAll());
    }
}
