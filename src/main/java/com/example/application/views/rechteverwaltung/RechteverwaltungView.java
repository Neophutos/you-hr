package com.example.application.views.rechteverwaltung;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.MitarbeiterService;
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
public class RechteverwaltungView extends Div implements BeforeEnterObserver {

    private final String RECHTEVERWALTUNG_ID = "rechteverwaltungID";
    private final String RECHTEVERWALTUNG_EDIT_ROUTE_TEMPLATE = "rechteverwaltung/%s/edit";

    private Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);

    private Checkbox mitarbeiterCheckbox;
    private Checkbox personalerCheckbox;
    private Checkbox adminCheckbox;
    private Checkbox loeschen;
    private Checkbox admin;

    private Button cancelButton = new Button("Abbrechen");
    private Button saveButton = new Button("Speichern");

    private BeanValidationBinder<Mitarbeiter> mitarbeiterBinder;

    private Mitarbeiter mitarbeiter;

    private final MitarbeiterService mitarbeiterService;


    @Autowired
    public RechteverwaltungView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;

        addClassNames("rechteverwaltung-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("vorname").setAutoWidth(true);
        grid.addColumn("nachname").setAutoWidth(true);
        grid.addColumn("id").setAutoWidth(true);


        LitRenderer<Mitarbeiter> mitarbeiterRenderer = LitRenderer.<Mitarbeiter>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Mitarbeiter") ? "check" : "minus").withProperty("color",
                        mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Mitarbeiter")
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(mitarbeiterRenderer).setHeader("Mitarbeiter").setAutoWidth(true);

        LitRenderer<Mitarbeiter> personalerRenderer = LitRenderer.<Mitarbeiter>of(
                "<vaadin-icon icon='vaadin:${item.icon}' sty" +
                        "le='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Personaler") ? "check" : "minus").withProperty("color",
                        mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Personaler")
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(personalerRenderer).setHeader("Personaler").setAutoWidth(true);

        LitRenderer<Mitarbeiter> adminRenderer = LitRenderer.<Mitarbeiter>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Admin") ? "check" : "minus").withProperty("color",
                        mitarbeiter -> mitarbeiter.getUser().getRoles().contains("Admin")
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(adminRenderer).setHeader("Admin").setAutoWidth(true);

        grid.setItems(query -> mitarbeiterService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(RECHTEVERWALTUNG_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));

                this.mitarbeiter = event.getValue();

            } else {
                clearForm();
                UI.getCurrent().navigate(RechteverwaltungView.class);
            }
        });

        // Configure Form
        mitarbeiterBinder = new BeanValidationBinder<>(Mitarbeiter.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.bindInstanceFields(this);
        mitarbeiterBinder.forField(mitarbeiterCheckbox).bind("user.getRoles() ");
        mitarbeiterBinder.forField(personalerCheckbox).bind("rechteverwaltung.erstellen");
        mitarbeiterBinder.forField(adminCheckbox).bind("rechteverwaltung.bearbeiten");
        mitarbeiterBinder.forField(loeschen).bind("rechteverwaltung.loeschen");
        mitarbeiterBinder.forField(admin).bind("rechteverwaltung.admin");

        mitarbeiterCheckbox.addValueChangeListener(event -> {
            if(!mitarbeiterCheckbox.getValue()){
                admin.setValue(false);
            }
        });

        personalerCheckbox.addValueChangeListener(event -> {
           if(!personalerCheckbox.getValue()){
               admin.setValue(false);
           }
        });

        adminCheckbox.addValueChangeListener(event -> {
            if(!adminCheckbox.getValue()){
                admin.setValue(false);
            }
        });

        loeschen.addValueChangeListener(event -> {
            if(!loeschen.getValue()){
                admin.setValue(false);
            }
        });

        admin.addValueChangeListener(event -> {
            if (admin.getValue()) {
                mitarbeiterCheckbox.setValue(true);
                personalerCheckbox.setValue(true);
                adminCheckbox.setValue(true);
                loeschen.setValue(true);
            } else {
                mitarbeiterCheckbox.setValue(false);
                personalerCheckbox.setValue(false);
                adminCheckbox.setValue(false);
                loeschen.setValue(false);
            }
        });

        cancelButton.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        saveButton.addClickListener(e -> {
            try {
                mitarbeiterBinder.writeBean(this.mitarbeiter);

                System.out.println(this.mitarbeiter);

                mitarbeiterService.update(this.mitarbeiter);
                clearForm();
                refreshGrid();
                System.out.println("Grid refreshed");
                Notification.show("Rechteverwaltung details stored.");
                UI.getCurrent().navigate(RechteverwaltungView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the rechteverwaltung details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> rechteverwaltungId = event.getRouteParameters().get(RECHTEVERWALTUNG_ID).map(Long::parseLong);
        if (rechteverwaltungId.isPresent()) {
            Optional<Mitarbeiter> rechteverwaltungFromBackend = mitarbeiterService
                    .get(rechteverwaltungId.get());
            if (rechteverwaltungFromBackend.isPresent()) {
                populateForm(rechteverwaltungFromBackend.get());
            } else {
                Notification.show(String.format("The requested rechteverwaltung was not found, ID = %s",
                        rechteverwaltungId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(RechteverwaltungView.class);
            }
        }
    }

    /**
     * @desc  Konfiguration der Ausrichtung & Form der Tabelle, Initialisierung der Interaktionselemente
     * @param splitLayout
     */

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        mitarbeiterCheckbox = new Checkbox("Lesen");
        personalerCheckbox = new Checkbox("Erstellen");
        adminCheckbox = new Checkbox("Bearbeiten");
        loeschen = new Checkbox("Loeschen");
        admin = new Checkbox("Admin");

        Component[] fields = new Component[]{mitarbeiterCheckbox, personalerCheckbox, adminCheckbox, loeschen, admin};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(saveButton, cancelButton);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Mitarbeiter value) {
        this.mitarbeiter = value;
        mitarbeiterBinder.readBean(this.mitarbeiter);
    }
}
