package com.example.application.views.problemmanagement;

import com.example.application.data.entity.Problem;
import com.example.application.data.service.ProblemformularService;
import com.example.application.views.MainLayout;
import com.example.application.views.problemformular.AntragView;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@PageTitle("Antragsverwaltung")
@Route(value = "antragsverwaltung", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AntragsVerwaltungView extends Div {
    Grid<Problem> grid = new Grid<>(Problem.class, false);
    TextField filterText = new TextField();
    Dialog confirmDialog = new Dialog();

    ProblemformularService service;

    @Autowired
    public AntragsVerwaltungView(ProblemformularService service) {
        this.service = service;
        addClassName("antragsverwaltungs-view");

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

        Button addProblem = new Button("Problem erstellen");
        Button editProblem = new Button("Gelöst?", e -> solveProblem(grid.asSingleSelect().getValue()));

        addProblem.addClickListener(e -> addProblem.getUI().ifPresent(ui -> ui.navigate(
                AntragView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(addProblem, editProblem);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void solveProblem(Problem problem) {
        if(problem == null) {
            Notification.show("Es wurde kein Problem ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            confirmDialog.setHeaderTitle("Antrag abschließen?");

            Button cancelButton = createCancelButton(confirmDialog);
            Button confirmButton = createConfirmButton(confirmDialog, problem);
            confirmDialog.getFooter().add(cancelButton);
            confirmDialog.getFooter().add(confirmButton);

            confirmDialog.open();
        }
    }

    private Button createCancelButton(Dialog confirmDialog) {
        Button cancelButton = new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Antragsbearbeitung wurde abgebrochen");
            updateList();
        });
        return cancelButton;
    }

    private Button createConfirmButton(Dialog confirmDialog, Problem problem) {
        Button saveButton = new Button("Abschließen", e -> {
            service.delete(problem);
            Notification.show("Antrag " + problem.getId() + " wurde erfolgreich abgeschlossen!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }


    private void configureGrid() {
        grid.addClassNames("problem-grid");
        grid.setSizeFull();
        grid.addColumn("id");
        grid.addColumn(problem -> problem.getDatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Erstellungsdatum");
        grid.addColumns("problemart","beschreibung");
        grid.addColumn("antragstellername").setHeader("Antragsteller");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList(){
        grid.setItems(service.findAllProblems());
    }

}
