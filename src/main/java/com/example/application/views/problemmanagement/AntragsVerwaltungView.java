package com.example.application.views.problemmanagement;

import com.example.application.data.entity.Antrag;
import com.example.application.data.service.AntragService;
import com.example.application.views.MainLayout;
import com.example.application.views.antrag.AntragView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@PageTitle("Anträge")
@Route(value = "antraege", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AntragsVerwaltungView extends Div {
    Grid<Antrag> grid = new Grid<>(Antrag.class, false);
    TextField filterText = new TextField();
    Dialog confirmDialog;

    Button cancelButton;
    Button confirmButton;

    AntragService service;

    @Autowired
    public AntragsVerwaltungView(AntragService service) {
        this.service = service;
        addClassName("antragsverwaltungs-view");

        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();

        GridContextMenu<Antrag> menu = grid.addContextMenu();
        menu.addItem("Abschließen", event -> solveAntrag(grid.asSingleSelect().getValue()));
    }

    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {

        Button addAntrag = new Button("Antrag erstellen");

        addAntrag.addClickListener(e -> addAntrag.getUI().ifPresent(ui -> ui.navigate(
                AntragView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(addAntrag);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    private static ComponentRenderer<AntragsVerwaltungView.AntragDetailsFormLayout, Antrag> createAntragDetailsRenderer() {
        return new ComponentRenderer<>(AntragsVerwaltungView.AntragDetailsFormLayout::new,
                AntragsVerwaltungView.AntragDetailsFormLayout::setAntrag);
    }

    private static class AntragDetailsFormLayout extends FormLayout {
        private final TextArea beschreibung = new TextArea("Beschreibung");

        public AntragDetailsFormLayout(){
            beschreibung.setMinWidth("400px");
            beschreibung.setMaxWidth("500px");
            beschreibung.setReadOnly(true);
            add(beschreibung);

            //setResponsiveSteps(new ResponsiveStep("0",3));
        }

        public void setAntrag(Antrag antrag) {
            beschreibung.setValue(antrag.getBeschreibung());

        }
    }

    private void solveAntrag(Antrag antrag) {
        if(antrag == null) {
            Notification.show("Es wurde kein Problem ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            confirmDialog = new Dialog();

            confirmDialog.setHeaderTitle("Antrag abschließen?");

            cancelButton = createCancelButton(confirmDialog);
            confirmButton = createConfirmButton(confirmDialog, antrag);
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

    private Button createConfirmButton(Dialog confirmDialog, Antrag antrag) {
        Button saveButton = new Button("Abschließen", e -> {
            service.delete(antrag);
            Notification.show("Antrag " + antrag.getId() + " wurde erfolgreich abgeschlossen!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
        grid.addColumn("antragsart");
        grid.addColumn("antragstellername").setHeader("Antragsteller");
        grid.setItemDetailsRenderer(createAntragDetailsRenderer());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList(){
        grid.setItems(service.findAll());
    }

}
