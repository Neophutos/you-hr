package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.HRService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.PersonformularView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@PageTitle("Mitarbeiterliste")
@Route(value = "mitarbeiterliste", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MitarbeiterlisteView extends Div {

    Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);
    TextField filterText = new TextField();
    HRService service;

    @Autowired
    public MitarbeiterlisteView(HRService service) {
        this.service = service;
        addClassName("mitarbeiterliste-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();
    }

    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Nach Name filtern...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addMitarbeiter = new Button("Mitarbeiter erstellen");

        addMitarbeiter.addClickListener(e -> addMitarbeiter.getUI().ifPresent(ui -> ui.navigate(
                PersonformularView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addMitarbeiter);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("mitarbeiter-grid");

        //grid.addColumn(mitarbeiter -> mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));

        grid.setSizeFull();
        grid.addColumns("vorname", "nachname", "email", "position", "abteilung", "adresse");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList(){
        grid.setItems(service.findAllMitarbeiter(filterText.getValue()));
    }
}
