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
    TaskService service;
    Dialog solveProblemDialog = new Dialog();

    @Autowired
    public ProblemManagementView(TaskService service) {
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
        Button editProblem = new Button("Bearbeiten", e -> solveProblemDialog.open());

        addProblem.addClickListener(e -> addProblem.getUI().ifPresent(ui -> ui.navigate(
                ProblemformularView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(addProblem, editProblem);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private static VerticalLayout createDialogLayout(Problem problem) {

        Text description = new Text(problem.getBeschreibung());
        Text type = new Text(problem.getProblemart());

        VerticalLayout dialogLayout = new VerticalLayout(type, description);

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");


        return dialogLayout;
    }

    private void configureGrid() {
        grid.addClassNames("problem-grid");
        grid.setSizeFull();
        grid.addColumn("id");
        //grid.addColumn(problem -> problem.getDatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        grid.addColumns("problemart","beschreibung");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> getCurrent(event.getValue()));
    }

    private void getCurrent(Problem problem) {
        if(problem != null) {

            VerticalLayout dialogLayout = createDialogLayout(problem);
            solveProblemDialog.add(dialogLayout);

            Button solveButton = new Button("Fertigstellen", e -> solveProblemDialog.close());
            Button cancelButton = new Button("Abbrechen", e -> solveProblemDialog.close());
            solveProblemDialog.add(solveButton);
            solveProblemDialog.add(cancelButton);

            solveProblemDialog.open();
        }
    }

    private void updateList(){
        grid.setItems(service.findAllProblems(filterText.getValue()));
    }

}
