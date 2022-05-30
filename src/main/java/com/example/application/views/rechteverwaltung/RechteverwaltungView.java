package com.example.application.views.rechteverwaltung;

import com.example.application.data.entity.Rechteverwaltung;
import com.example.application.data.service.RechteverwaltungService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Rechteverwaltung")
@Route(value = "rechteverwaltung/:rechteverwaltungID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
@Uses(Icon.class)
@Uses(Icon.class)
@Uses(Icon.class)
@Uses(Icon.class)
public class RechteverwaltungView extends Div implements BeforeEnterObserver {

    private final String RECHTEVERWALTUNG_ID = "rechteverwaltungID";
    private final String RECHTEVERWALTUNG_EDIT_ROUTE_TEMPLATE = "rechteverwaltung/%s/edit";

    private Grid<Rechteverwaltung> grid = new Grid<>(Rechteverwaltung.class, false);

    private TextField vorname;
    private TextField nachname;
    private TextField mitarbeiterid;
    private Checkbox lesen;
    private Checkbox erstellen;
    private Checkbox bearbeiten;
    private Checkbox loeschen;
    private Checkbox admin;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Rechteverwaltung> binder;

    private Rechteverwaltung rechteverwaltung;

    private final RechteverwaltungService rechteverwaltungService;

    @Autowired
    public RechteverwaltungView(RechteverwaltungService rechteverwaltungService) {
        this.rechteverwaltungService = rechteverwaltungService;
        addClassNames("rechteverwaltung-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("vorname").setAutoWidth(true);
        grid.addColumn("nachname").setAutoWidth(true);
        grid.addColumn("mitarbeiterid").setAutoWidth(true);
        LitRenderer<Rechteverwaltung> lesenRenderer = LitRenderer.<Rechteverwaltung>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", lesen -> lesen.isLesen() ? "check" : "minus").withProperty("color",
                        lesen -> lesen.isLesen()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(lesenRenderer).setHeader("Lesen").setAutoWidth(true);

        LitRenderer<Rechteverwaltung> erstellenRenderer = LitRenderer.<Rechteverwaltung>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", erstellen -> erstellen.isErstellen() ? "check" : "minus").withProperty("color",
                        erstellen -> erstellen.isErstellen()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(erstellenRenderer).setHeader("Erstellen").setAutoWidth(true);

        LitRenderer<Rechteverwaltung> bearbeitenRenderer = LitRenderer.<Rechteverwaltung>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", bearbeiten -> bearbeiten.isBearbeiten() ? "check" : "minus").withProperty("color",
                        bearbeiten -> bearbeiten.isBearbeiten()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(bearbeitenRenderer).setHeader("Bearbeiten").setAutoWidth(true);

        LitRenderer<Rechteverwaltung> loeschenRenderer = LitRenderer.<Rechteverwaltung>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", loeschen -> loeschen.isLoeschen() ? "check" : "minus").withProperty("color",
                        loeschen -> loeschen.isLoeschen()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(loeschenRenderer).setHeader("Loeschen").setAutoWidth(true);

        LitRenderer<Rechteverwaltung> adminRenderer = LitRenderer.<Rechteverwaltung>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", admin -> admin.isAdmin() ? "check" : "minus").withProperty("color",
                        admin -> admin.isAdmin()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(adminRenderer).setHeader("Admin").setAutoWidth(true);

        grid.setItems(query -> rechteverwaltungService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(RECHTEVERWALTUNG_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(RechteverwaltungView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Rechteverwaltung.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.rechteverwaltung == null) {
                    this.rechteverwaltung = new Rechteverwaltung();
                }
                binder.writeBean(this.rechteverwaltung);

                rechteverwaltungService.update(this.rechteverwaltung);
                clearForm();
                refreshGrid();
                Notification.show("Rechteverwaltung details stored.");
                UI.getCurrent().navigate(RechteverwaltungView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the rechteverwaltung details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> rechteverwaltungId = event.getRouteParameters().get(RECHTEVERWALTUNG_ID).map(UUID::fromString);
        if (rechteverwaltungId.isPresent()) {
            Optional<Rechteverwaltung> rechteverwaltungFromBackend = rechteverwaltungService
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

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        vorname = new TextField("Vorname");
        nachname = new TextField("Nachname");
        mitarbeiterid = new TextField("Mitarbeiterid");
        lesen = new Checkbox("Lesen");
        erstellen = new Checkbox("Erstellen");
        bearbeiten = new Checkbox("Bearbeiten");
        loeschen = new Checkbox("Loeschen");
        admin = new Checkbox("Admin");
        Component[] fields = new Component[]{vorname, nachname, mitarbeiterid, lesen, erstellen, bearbeiten, loeschen,
                admin};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
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

    private void populateForm(Rechteverwaltung value) {
        this.rechteverwaltung = value;
        binder.readBean(this.rechteverwaltung);

    }
}
