package com.youhr.application.views;

import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Antrag;
import com.youhr.application.data.entity.Mitarbeiter;
import com.youhr.application.data.entity.Status;
import com.youhr.application.data.service.AntragService;
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
import com.youhr.application.forms.AbteilungForm;
import com.youhr.application.forms.StatusForm;
import com.youhr.application.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @desc Der View Antragsverwaltung implementiert eine Tabellenansicht für alle vorhandenen Anträge.
 *
 * @category View
 * @author Ben Köppe, Tim Freund, Chris Zobel
 * @version 1.0
 * @since 2022-07-30
 */
@PageTitle("Anträge | YOU-HR")
@Route(value = "antraege", layout = MainLayout.class)
@RolesAllowed({"PERSONALER", "ADMIN"})
public class AntragVerwaltungView extends Div {
    Grid<Antrag> grid = new Grid<>(Antrag.class, false);
    Dialog confirmDialog;

    Dialog editStatusDialog = new Dialog();

    StatusForm statusForm;

    Button cancelButton;
    Button confirmButton;

    private final AntragService antragService;

    /**
     * @desc Initialisierung des grafischen Interfaces und des Rechtsklick-Menüs
     * @param antragService
     */
    @Autowired
    public AntragVerwaltungView(AntragService antragService) {
        this.antragService = antragService;
        addClassName("antragsverwaltungs-view");

        setSizeFull();
        configureForm();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();

        VerticalLayout editStatusDialogLayout = createEditStatusDialogLayout();
        editStatusDialog.add(editStatusDialogLayout);

        GridContextMenu<Antrag> menu = grid.addContextMenu();

        GridMenuItem<Antrag> changeStatus = menu.addItem("Status ändern", event -> editStatus(grid.asSingleSelect().getValue()));
        changeStatus.addComponentAsFirst(createIcon(VaadinIcon.CLOCK));

        GridMenuItem<Antrag> complete = menu.addItem("Abschließen", event -> solveAntrag(grid.asSingleSelect().getValue()));
        complete.addComponentAsFirst(createIcon(VaadinIcon.CLIPBOARD_CHECK));
    }

    /**
     * @desc Grafische Konfiguration der Icons im Rechtsklick-Menü
     * @param vaadinIcon
     */
    private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
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
        Button addAntrag = new Button("Antrag erstellen", new Icon(VaadinIcon.CLIPBOARD_USER));
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
        grid.addColumn("status");
        grid.addColumn("antragsart");
        grid.addColumn("antragstellername").setHeader("Antragsteller");
        grid.setItemDetailsRenderer(createAntragDetailsRenderer());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureForm(){
        statusForm = new StatusForm();
        statusForm.addListener(StatusForm.SaveEvent.class, this::saveAntrag);
        statusForm.addListener(StatusForm.CloseEvent.class, e -> editStatusDialog.close());
    }

    /**
     * @desc Erstellung des Erstellungs- und Bearbeitungslayouts für Abteilungen.
     */
    private VerticalLayout createEditStatusDialogLayout(){
        VerticalLayout editDialogLayout = new VerticalLayout(statusForm);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    /**
     * @desc Öffnen des Abteilungformulars zur Bearbeitungs (oder Erstellung) einer Abteilung
     * @see StatusForm
     * @param antrag
     */
    public void editStatus(Antrag antrag){
        if(antrag == null) {
            Notification.show("Es wurde keine Abteilung ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            statusForm.setSelectedAntrag(antrag);
            editStatusDialog.open();
        }
    }

    /**
     * @desc Speicher-Event für Abteilungen
     * @param event
     */
    private void saveAntrag(StatusForm.SaveEvent event){
        antragService.update(event.getAntrag());
        updateList();
        editStatusDialog.close();
    }

    /**
     * @desc Update der Tabelle -> Nutzung bei durchgeführten Änderungen in der Tabelle
     */
    private void updateList(){
        grid.setItems(antragService.findAll());
    }

}
