package com.youhr.application.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.youhr.application.data.entity.Antrag;
import com.youhr.application.data.generator.DataGenerator;
import com.youhr.application.data.service.AntragService;
import com.youhr.application.security.AuthenticatedUser;
import com.youhr.application.views.DashboardView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * @desc Das Formular Antrag erstellt eine Eingabemaske für die Erstellung eines Objekts Antrag.
 *
 * @category Form
 * @author Ben Köppe
 * @version 1.0
 * @since 2022-06-30
 */
public class AntragForm extends FormLayout {

    private final BeanValidationBinder<Antrag> antragBinder = new BeanValidationBinder<>(Antrag.class);
    private final AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());

    Button absenden = new Button("Abschicken");
    Button schliessen = new Button("Schließen");

    int charLimit = 250;

    private final AntragService antragService;

    /**
     * @desc Binden der Eingabefelder an die Attribute des Objekts. Außerdem wird das Formular (Text + Eingabefelder + Buttons) initialisiert.
     * @param antragService
     */
    @Autowired
    public AntragForm(AntragService antragService) {
        this.antragService = antragService;
        addClassName("Antrag-Formular");

        antragBinder.bindInstanceFields(this);

        ComboBox<String> antragsart = new ComboBox<>("Antragsart");
        antragsart.setItems("Daten-Änderung", "Rechte-Änderung", "Problem-Meldung", "Anderes Anliegen");

        TextArea beschreibung = new TextArea("Problembeschreibung");
        beschreibung.setPlaceholder("Beschreiben Sie hier Ihr Anliegen möglichst präzise und genau.");
        beschreibung.setMinHeight("200px");
        beschreibung.setMaxLength(charLimit);
        beschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        beschreibung.addValueChangeListener(e -> {e.getSource().setHelperText(e.getValue().length() + "/" + charLimit);});

        add(antragsart, beschreibung, createButtonLayout());
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
            Antrag antrag = new Antrag();

            antrag.setDatum(LocalDate.now());

            antrag.setAntragstellername(authenticatedUser.get().get().getName());

            antragBinder.writeBean(antrag);

            antragService.update(antrag);

            Notification.show("Ihr Problem wurde erfolgreich gemeldet!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate(DashboardView.class);

        } catch (ValidationException e) {
            Notification.show("Ein Fehler ist aufgetreten!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
