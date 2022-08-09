package com.youhr.application.forms;

import com.youhr.application.data.entity.Antrag;
import com.youhr.application.data.generator.DataGenerator;
import com.youhr.application.data.service.AntragService;
import com.youhr.application.security.AuthenticatedUser;
import com.youhr.application.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * @desc Das Formular Antrag erstellt eine Eingabemaske für die Erstellung eines Objekts Antrag.
 *
 * @category Form
 * @version 1.0
 * @since 2022-06-30
 */
public class AntragForm extends FormLayout {

    private BeanValidationBinder<Antrag> antragBinder = new BeanValidationBinder<>(Antrag.class);
    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());

    Text text = new Text("Wählen Sie die Art des Antrags aus!");
    private ComboBox<String> antragsart = new ComboBox<>("Antragsart");
    private TextArea beschreibung = new TextArea("Problembeschreibung");

    Button absenden = new Button("Abschicken");
    Button schliessen = new Button("Schließen");

    int charLimit = 250;

    private AntragService antragService;

    private Antrag antrag;

    /**
     * @desc Binden der Eingabefelder an die Attribute des Objekts. Außerdem wird das Formular (Text + Eingabefelder + Buttons) initialisiert.
     * @param antragService
     */
    @Autowired
    public AntragForm(AntragService antragService) {
        this.antragService = antragService;
        addClassName("Antrag-Formular");

        antragBinder.bindInstanceFields(this);

        antragsart.setItems("Daten-Änderung", "Rechte-Änderung", "Problem-Meldung", "Anderes Anliegen");
        antragsart.setItems("Daten-Änderung", "Rechte-Änderung", "Problem-Meldung", "Anderes Anliegen");

        beschreibung.setPlaceholder("Beschreiben Sie hier Ihr Anliegen möglichst präzise und genau.");
        beschreibung.setMinHeight("200px");
        beschreibung.setMaxLength(charLimit);
        beschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        beschreibung.addValueChangeListener(e -> {e.getSource().setHelperText(e.getValue().length() + "/" + charLimit);});

        add(
                new H5("In diesem Formular können Sie Anträge zu folgenden Anliegen stellen:"),
                new H6("-> Änderung der persönlichen Daten"),
                new H6("-> Änderung der Lesen- und Bearbeitungsrechte (für Personaler vorbehalten)"),
                new H6("-> Meldung eines systemrelevanten Problems"),
                antragsart,
                beschreibung,
                createButtonLayout()
        );
    }

    /**
     * @desc Konfiguration der Ausrichtung der Buttons und deren Event bei Betätigung (Klick).
     */
    private Component createButtonLayout() {
        absenden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        absenden.addClickListener(event -> checkundSend());
        schliessen.addClickListener(event -> schliessen.getUI().ifPresent(ui ->
                ui.navigate("dashboard")));

        absenden.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        antragBinder.addStatusChangeListener(e -> absenden.setEnabled(antragBinder.isValid()));

        return new HorizontalLayout(absenden, schliessen);
    }

    /**
     * @desc Diese Methode erstellt bei Aufruf ein neues Objekt Antrag. Es liest dabei die Eingabefelder, das heutige Datum und den verursachenden Nutzer (Antragsteller) aus.
     */
    private void checkundSend() {
        try {
            this.antrag = new Antrag();

            this.antrag.setDatum(LocalDate.now());

            this.antrag.setAntragstellername(authenticatedUser.get().get().getName());

            antragBinder.writeBean(antrag);

            antragService.update(antrag);

            Notification.show("Ihr Problem wurde erfolgreich gemeldet!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate(DashboardView.class);

        } catch (ValidationException e) {
            Notification.show("Ein Fehler ist aufgetreten!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}