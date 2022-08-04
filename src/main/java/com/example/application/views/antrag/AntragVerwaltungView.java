package com.example.application.views.antrag;

import com.example.application.data.entity.Antrag;
import com.example.application.data.service.AntragService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.*;
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

/**
 * @desc Der View Antragsverwaltung implementiert eine Tabellenansicht für alle vorhandenen Anträge.
 *
 * @category View
 * @version 1.0
 * @since 2022-06-30
 */
@PageTitle("Anträge")
@Route(value = "antraege", layout = MainLayout.class)
@RolesAllowed({"PERSONALER", "ADMIN"})
public class AntragVerwaltungView extends Div {
    Grid<Antrag> grid = new Grid<>(Antrag.class, false);
    TextField filterText = new TextField();
    Dialog confirmDialog;

    Button cancelButton;
    Button confirmButton;

    AntragService antragService;

    /**
     * @desc Initialisierung des grafischen Interfaces und des Rechtsklick-Menüs
     * @param antragService
     */
    @Autowired
    public AntragVerwaltungView(AntragService antragService) {
        this.antragService = antragService;
        addClassName("antragsverwaltungs-view");

        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();

        GridContextMenu<Antrag> menu = grid.addContextMenu();
        menu.addItem("Abschließen", event -> solveAntrag(grid.asSingleSelect().getValue()));
    }

    /**
     * @desc Konfiguration der Ausrichtung und Form der Tabelle.
     */
    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    /**
     * @desc Konfiguration und Initialisierung der Interaktionselemente über der Tabelle
     */
    private HorizontalLayout getToolbar() {
        Button addAntrag = new Button("Antrag erstellen");
        Paragraph antragszahl = new Paragraph("Anzahl Anträge: " + antragService.countProblems());
        addAntrag.addClickListener(e -> addAntrag.getUI().ifPresent(ui -> ui.navigate(
                AntragView.class)));
        HorizontalLayout toolbar = new HorizontalLayout(addAntrag, antragszahl);
        toolbar.addClassName("toolbar");
        toolbar.setMargin(true);
        return toolbar;
    }

    private static ComponentRenderer<AntragVerwaltungView.AntragDetailsFormLayout, Antrag> createAntragDetailsRenderer() {
        return new ComponentRenderer<>(AntragVerwaltungView.AntragDetailsFormLayout::new,
                AntragVerwaltungView.AntragDetailsFormLayout::setAntrag);
    }

    /**
     * @desc Initialisierung der Detailansicht des ausgewählten Antrags
     */
    private static class AntragDetailsFormLayout extends FormLayout {
        private final TextArea beschreibung = new TextArea("Beschreibung");

        /**
         * @desc Grafische Konfiguration des Detail-Layouts
         */
        public AntragDetailsFormLayout(){
            beschreibung.setMinWidth("400px");
            beschreibung.setMaxWidth("500px");
            beschreibung.setReadOnly(true);
            add(beschreibung);
        }

        /**
         * @desc Setzen des ausgewählten Antrags
         * @param antrag
         */
        public void setAntrag(Antrag antrag) {
            beschreibung.setValue(antrag.getBeschreibung());
        }
    }

    /**
     * @desc Methode zum Lösen eines vorhandenen Antrags -> Löschen bei Abschluss und Information an Nutzer
     * @param antrag
     */
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

    /**
     * @desc Erstellung des Abbruch-Buttons und deren Logik bei Abbruch des Abschlusses
     */
    private Button createCancelButton(Dialog confirmDialog) {
        Button cancelButton = new Button("Abbrechen", e -> {
            confirmDialog.close();
            Notification.show("Antragsbearbeitung wurde abgebrochen");
            updateList();
        });
        return cancelButton;
    }

    /**
     * @desc Erstellung des Bestätigungs-Buttons und deren Logik bei Bestätigung des Abschlusses
     */
    private Button createConfirmButton(Dialog confirmDialog, Antrag antrag) {
        Button saveButton = new Button("Abschließen", e -> {
            antragService.delete(antrag);
            Notification.show("Antrag " + antrag.getId() + " wurde erfolgreich abgeschlossen!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    /**
     * @desc Konfiguration der Tabelle -> Setzen der Spalten und deren Inhalte
     */
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

    /**
     * @desc Update der Tabelle -> Nutzung bei durchgeführten Änderungen in der Tabelle
     */
    private void updateList(){
        grid.setItems(antragService.findAll());
    }

}
