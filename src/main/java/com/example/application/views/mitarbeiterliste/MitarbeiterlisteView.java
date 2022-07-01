package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problem;
import com.example.application.data.service.HRService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.MitarbeiterForm;
import com.example.application.views.personformular.PersonformularView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
    Dialog confirmDialog = new Dialog();
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
        Button editMitarbeiter = new Button("Bearbeiten");
        Button removeMitarbeiter = new Button("Löschen");

        addMitarbeiter.addClickListener(e -> addMitarbeiter.getUI().ifPresent(ui -> ui.navigate(
                PersonformularView.class)));

        editMitarbeiter.addClickListener(e -> editMitarbeiter(grid.asSingleSelect().getValue()));

        removeMitarbeiter.addClickListener(e -> removeMitarbeiter(grid.asSingleSelect().getValue()));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addMitarbeiter, editMitarbeiter, removeMitarbeiter);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    private void editMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null){
            
        }
    }

    private void configureGrid() {
        grid.addClassNames("mitarbeiter-grid");

        //grid.addColumn(mitarbeiter -> mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));

        grid.setSizeFull();
        grid.addColumns("vorname", "nachname", "email", "position", "abteilung", "adresse");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void removeMitarbeiter(Mitarbeiter mitarbeiter) {
        if(mitarbeiter == null) {
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            confirmDialog.setHeaderTitle("Mitarbeiter wirklich löschen?");

            Button cancelButton = createCancelButton(confirmDialog);
            Button confirmButton = createConfirmButton(confirmDialog, mitarbeiter);
            confirmDialog.getFooter().add(cancelButton);
            confirmDialog.getFooter().add(confirmButton);

            confirmDialog.open();
        }
    }

    private Button createCancelButton(Dialog confirmDialog) {
        Button cancelButton = new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Löschen wurde abgebrochen");
            updateList();
        });
        return cancelButton;
    }

    private Button createConfirmButton(Dialog confirmDialog, Mitarbeiter mitarbeiter) {
        Button saveButton = new Button("Abschließen", e -> {
            service.deleteMitarbeiter(mitarbeiter);
            Notification.show("Antrag " + mitarbeiter.getNachname() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }


    private void updateList(){
        grid.setItems(service.findAllMitarbeiter(filterText.getValue()));
    }
}
