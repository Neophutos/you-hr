package com.example.application.views.problemmanagement;

import com.example.application.data.entity.Problem;
import com.example.application.data.repository.ProblemformularRepository;
import com.example.application.data.service.ProblemformularService;
import com.example.application.data.service.TaskService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.PersonformularView;
import com.example.application.views.problemformular.ProblemformularView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@PageTitle("Problem-Management")
@Route(value = "problem-management", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ProblemManagementView extends Div {
    Grid<Problem> grid = new Grid<>(Problem.class, false);
    TextField filterText = new TextField();
    ProblemformularService service;

    @Autowired
    public ProblemManagementView(ProblemformularService service) {
        this.service = service;
        addClassName("problem-management-view");

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

        Button addProblem = new Button("Problem erstellen");
        Button editProblem = new Button("Gelöst?", e -> solveProblem(grid.asSingleSelect().getValue()));

        addProblem.addClickListener(e -> addProblem.getUI().ifPresent(ui -> ui.navigate(
                ProblemformularView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(addProblem, editProblem);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void solveProblem(Problem problem) {
        if(problem == null) {
            Notification.show("Es wurde kein Problem ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            service.delete(problem);
            Notification.show("Problem " + problem.getId() + " wurde erfolgreich gelöst!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            updateList();
        }
    }


    private void configureGrid() {
        grid.addClassNames("problem-grid");
        grid.setSizeFull();
        grid.addColumn("id");
        grid.addColumn(problem -> problem.getDatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Erstellungsdatum");
        grid.addColumns("problemart","beschreibung","antragsteller");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList(){
        grid.setItems(service.findAllProblems());
    }

}
