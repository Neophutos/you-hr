package com.example.application.views.mitarbeiterliste;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Stream;

@PageTitle("Mitarbeiterliste")
@Route(value = "mitarbeiterliste", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@JsModule("./modules/copytoclipboard.js")
public class MitarbeiterlisteView extends Div {

    Grid<Mitarbeiter> grid = new Grid<>(Mitarbeiter.class, false);
    TextField filterText = new TextField();
    Dialog editDialog = new Dialog();
    Dialog deletionDialog;

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

        GridContextMenu<Mitarbeiter> menu = grid.addContextMenu();
        GridMenuItem<Mitarbeiter> mail = menu.addItem("E-Mail", event -> getMail(grid.asSingleSelect().getValue()));
        mail.addComponentAsFirst(createIcon(VaadinIcon.ENVELOPES_O));
        menu.add(new Hr());
        GridMenuItem<Mitarbeiter> bearbeiten = menu.addItem("Bearbeiten", event -> editMitarbeiter(grid.asSingleSelect().getValue()));
        bearbeiten.addComponentAsFirst(createIcon(VaadinIcon.EDIT));
        GridMenuItem<Mitarbeiter> loeschen = menu.addItem("Löschen", event -> removeMitarbeiter(grid.asSingleSelect().getValue()));
        loeschen.addComponentAsFirst(createIcon(VaadinIcon.ERASER));

    }

    private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

    private void getMail(Mitarbeiter mitarbeiter){
        UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", mitarbeiter.getEmail());
        Notification.show("Die E-Mail von " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname() + " wurde kopiert!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private VerticalLayout createEditDialogLayout() {
        VerticalLayout editDialogLayout = new VerticalLayout(form);
        editDialogLayout.setPadding(false);
        editDialogLayout.setSpacing(false);
        editDialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return editDialogLayout;
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new MitarbeiterForm(mitarbeiterService.findAllAbteilungen(), mitarbeiterService.findAllTeams());
        form.addListener(MitarbeiterForm.SaveEvent.class, this::saveMitarbeiter);
        form.addListener(MitarbeiterForm.CloseEvent.class, e -> editDialog.close());
    }

    private void saveMitarbeiter(MitarbeiterForm.SaveEvent event) {
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
        filterText.setWidth("300px");

        Button addMitarbeiter = new Button("Hinzufügen");

        addMitarbeiter.addClickListener(e -> addMitarbeiter());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addMitarbeiter);
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
        if (mitarbeiter == null) {
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            form.setSelectedMitarbeiter(mitarbeiter);
            editDialog.open();
        }
    }

    private void configureGrid() {
        grid.addClassNames("mitarbeiter-grid");
        grid.setSizeFull();
        grid.addColumns("vorname", "nachname", "email", "abteilung");

        grid.setItemDetailsRenderer(createMitarbeiterDetailsRenderer());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private static ComponentRenderer<MitarbeiterDetailsFormLayout, Mitarbeiter> createMitarbeiterDetailsRenderer() {
        return new ComponentRenderer<>(MitarbeiterDetailsFormLayout::new,
                MitarbeiterDetailsFormLayout::setMitarbeiter);
    }

    private static class MitarbeiterDetailsFormLayout extends FormLayout {
        private H6 personal = new H6("Persönliche Angaben");
        private H6 beruf = new H6("Berufliche Angaben");
        private H6 kontakt = new H6("Kontakt");

        private final TextField name = new TextField("Name");
        private final TextField geburtsdatum = new TextField("Geburtsdatum");
        private final TextField email = new TextField("Email");
        private final TextField telefonnr = new TextField("Telefon");
        private final TextField position = new TextField("Position");
        private final TextField abteilung = new TextField("Abteilung");
        private final TextField team = new TextField("Team");
        private final TextField adresse = new TextField("Anschrift");

        public MitarbeiterDetailsFormLayout() {
            Stream.of(name, geburtsdatum, email, telefonnr, adresse, position, abteilung).forEach(field -> {
                field.setReadOnly(true);
            });

            add(
                personal,
                name,
                geburtsdatum,
                beruf,
                position,
                abteilung,
                kontakt,
                telefonnr,
                email,
                adresse
            );

            setColspan(personal, 2);
            setColspan(beruf, 2);
            setColspan(kontakt, 2);
            setColspan(adresse, 2);
            setMaxWidth("600px");
            setResponsiveSteps(new ResponsiveStep("0", 2));
        }

        public void setMitarbeiter(Mitarbeiter mitarbeiter) {
            name.setValue(mitarbeiter.getVorname() + " " + mitarbeiter.getNachname());
            geburtsdatum.setValue(mitarbeiter.getGeburtsdatum().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            email.setValue(mitarbeiter.getEmail());
            telefonnr.setValue(mitarbeiter.getTelefonnr());
            position.setValue(mitarbeiter.getPosition());
            abteilung.setValue(mitarbeiter.getAbteilung().toString());
            team.setValue(mitarbeiter.getTeam().toString());
            adresse.setValue(mitarbeiter.getAdresse().toString());
        }
    }

    private void removeMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null) {
            Notification.show("Es wurde kein Mitarbeiter ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            deletionDialog = new Dialog();

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
            Notification.show("Vorgang wurde abgebrochen");
            updateList();
        });
    }

    private Button createConfirmButton(Dialog confirmDialog, Mitarbeiter mitarbeiter) {
        Button saveButton = new Button("Abschließen", e -> {
            mitarbeiterService.delete(mitarbeiter);
            Notification.show("Mitarbeiter " + mitarbeiter.getNachname() + " wurde erfolgreich gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
            updateList();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }


    private void updateList() {
        grid.setItems(mitarbeiterService.findAllByString(filterText.getValue()));
    }
}
