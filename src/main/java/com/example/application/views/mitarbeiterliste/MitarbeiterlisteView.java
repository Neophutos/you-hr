package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.User;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.MitarbeiterForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@PageTitle("Mitarbeiterliste")
@Route(value = "mitarbeiterliste", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MitarbeiterlisteView extends Div {

    Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);
    TextField filterText = new TextField();
    Dialog editDialog = new Dialog();

    Button cancelButton;
    Button confirmButton;

    MitarbeiterForm form;
    MitarbeiterService mitarbeiterService;

    Mitarbeiter mitarbeiter;

    @Autowired
    public MitarbeiterlisteView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassName("mitarbeiterliste-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();

        VerticalLayout editDialogLayout = createEditDialogLayout();
        editDialog.add(editDialogLayout);
    }

    private VerticalLayout createEditDialogLayout() {
        VerticalLayout editDialogLayout = new VerticalLayout(form);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    private HorizontalLayout getContent(){
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm(){
        form = new MitarbeiterForm();
        form.addListener(MitarbeiterForm.SaveEvent.class, this::saveMitarbeiter);
        form.addListener(MitarbeiterForm.CloseEvent.class, e -> editDialog.close());
    }

    private void saveMitarbeiter(MitarbeiterForm.SaveEvent event){
        event.getMitarbeiter().generateUser();
        mitarbeiterService.update(event.getMitarbeiter());
        updateList();
        editDialog.close();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Nach Name filtern...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addMitarbeiter = new Button("Hinzufügen");
        Button editMitarbeiter = new Button("Bearbeiten");
        Button removeMitarbeiter = new Button("Löschen");

        addMitarbeiter.addClickListener(e -> addMitarbeiter());

        editMitarbeiter.addClickListener(e -> editMitarbeiter(grid.asSingleSelect().getValue()));

        removeMitarbeiter.addClickListener(e -> removeMitarbeiter(grid.asSingleSelect().getValue()));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addMitarbeiter, editMitarbeiter, removeMitarbeiter);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    private void addMitarbeiter() {
        grid.asSingleSelect().clear();
        mitarbeiter = new Mitarbeiter();
        Adresse adresse = new Adresse();
        mitarbeiter.setAdresse(adresse);
        editMitarbeiter(mitarbeiter);
    }

    private void editMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null){
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            form.setSelectedMitarbeiter(mitarbeiter);
            editDialog.open();
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
            Dialog deletionDialog = new Dialog();

            deletionDialog.setHeaderTitle("Mitarbeiter wirklich löschen?");

            cancelButton = createCancelButton(deletionDialog);
            deletionDialog.getFooter().add(cancelButton);
            confirmButton = createConfirmButton(deletionDialog, mitarbeiter);
            deletionDialog.getFooter().add(confirmButton);


            deletionDialog.open();
        }
    }

    private Button createCancelButton(Dialog confirmDialog) {
        return new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Vorgang wurde abgebrochen.");
            updateList();
        });
    }

    private Button createConfirmButton(Dialog confirmDialog, Mitarbeiter mitarbeiter) {
        Button saveButton = new Button("Abschließen", e -> {
            mitarbeiterService.delete(mitarbeiter);
            //service.deleteMitarbeiter(mitarbeiter);
            Notification.show("Mitarbeiter " + mitarbeiter.getNachname() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }


    private void updateList(){
        grid.setItems(mitarbeiterService.findAllByString(filterText.getValue()));
        //grid.setItems(service.findAllMitarbeiter(filterText.getValue()));
    }
}
