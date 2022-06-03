package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.PersonformularView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Mitarbeiterliste")
@Route(value = "mitarbeiterliste/:mitarbeiterID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MitarbeiterlisteView extends Div implements BeforeEnterObserver {

    private final String MITARBEITER_ID = "mitarbeiterID";
    private final String MITARBEITER_EDIT_ROUTE_TEMPLATE = "mitarbeiterliste/%s/edit";

    private Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);

    private Button create = new Button("Erstellen");
    private Button delete = new Button("Entfernen");

    private TextField mitarbeiterid;
    private TextField vorname;
    private TextField nachname;
    private TextField email;
    private TextField telefonnr;
    private TextField position;
    private TextField abteilung;
    private TextField adresse;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Mitarbeiter> binder;

    private Mitarbeiter mitarbeiter;

    private final MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterlisteView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassNames("mitarbeiterliste-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(createButtonLayout());
        add(splitLayout);

        create.addClickListener(e -> create.getUI().ifPresent(ui -> ui.navigate(
                PersonformularView.class)));

        // Configure Grid
        grid.addColumn("mitarbeiterid").setAutoWidth(true);
        grid.addColumn("vorname").setAutoWidth(true);
        grid.addColumn("nachname").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("telefonnr").setAutoWidth(true);
        grid.addColumn("position").setAutoWidth(true);
        grid.addColumn("abteilung").setAutoWidth(true);
        grid.addColumn("adresse").setAutoWidth(true);
        grid.setItems(query -> mitarbeiterService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MITARBEITER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MitarbeiterlisteView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Mitarbeiter.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.mitarbeiter == null) {
                    this.mitarbeiter = new Mitarbeiter();
                }
                binder.writeBean(this.mitarbeiter);

                mitarbeiterService.update(this.mitarbeiter);
                clearForm();
                refreshGrid();
                Notification.show("Mitarbeiter details stored.");
                UI.getCurrent().navigate(MitarbeiterlisteView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the mitarbeiter details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> mitarbeiterId = event.getRouteParameters().get(MITARBEITER_ID).map(UUID::fromString);
        if (mitarbeiterId.isPresent()) {
            Optional<Mitarbeiter> mitarbeiterFromBackend = mitarbeiterService.get(mitarbeiterId.get());
            if (mitarbeiterFromBackend.isPresent()) {
                populateForm(mitarbeiterFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested mitarbeiter was not found, ID = %s", mitarbeiterId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MitarbeiterlisteView.class);
            }
        }
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(create);
        buttonLayout.add(delete);
        return buttonLayout;
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        mitarbeiterid = new TextField("Mitarbeiterid");
        vorname = new TextField("Vorname");
        nachname = new TextField("Nachname");
        email = new TextField("Email");
        telefonnr = new TextField("Telefonnr");
        position = new TextField("Position");
        abteilung = new TextField("Abteilung");
        adresse = new TextField("Adresse");
        Component[] fields = new Component[]{mitarbeiterid, vorname, nachname, email, telefonnr, position, abteilung,
                adresse};

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

    private void populateForm(Mitarbeiter value) {
        this.mitarbeiter = value;
        binder.readBean(this.mitarbeiter);

    }
}
