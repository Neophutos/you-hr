package com.example.application.views.personformular;

import com.example.application.data.entity.Mitarbeiter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class MitarbeiterForm extends FormLayout {
    Binder<Mitarbeiter> binder = new BeanValidationBinder<>(Mitarbeiter.class);

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private EmailField email = new EmailField("E-Mail");
    private TextField geburtsdatum = new TextField("Geburtstag");
    private TextField telefonnr = new TextField("Telefonnummer");
    private TextField position = new TextField("Position");
    private ComboBox<String> abteilung = new ComboBox<>("Abteilung");

    Button speichern = new Button("Speichern");
    Button schliessen = new Button("Schließen");

    private Mitarbeiter mitarbeiter;

    public MitarbeiterForm() {
        addClassName("Mitarbeiter-Formular");

        binder.bindInstanceFields(this);

        abteilung.setItems("Buchhaltung","Forschung & Entwicklung","Geschäftsleitung","IT & EDV","Kundendienst", "Marketing", "Personalwesen");

        add(
                vorname,
                nachname,
                email,
                geburtsdatum,
                telefonnr,
                position,
                abteilung,
                createButtonLayout()
        );
    }


    public void setMitarbeiter(Mitarbeiter mitarbeiter){
        this.mitarbeiter = mitarbeiter;
        binder.readBean(mitarbeiter);
    }

    private Component createButtonLayout() {
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        speichern.addClickListener(event -> checkUndSpeichern());
        schliessen.addClickListener(event -> fireEvent(new CloseEvent(this)));

        speichern.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> speichern.setEnabled(binder.isValid()));

        return new HorizontalLayout(speichern, schliessen);
    }

    private void checkUndSpeichern() {
        try {
            binder.writeBean(mitarbeiter);
            fireEvent(new SaveEvent(this, mitarbeiter));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class MitarbeiterFormEvent extends ComponentEvent<MitarbeiterForm> {
        private Mitarbeiter mitarbeiter;

        protected MitarbeiterFormEvent(MitarbeiterForm source, Mitarbeiter mitarbeiter) {
            super(source, false);
            this.mitarbeiter = mitarbeiter;
        }

        public Mitarbeiter getMitarbeiter() {
            return mitarbeiter;
        }
    }

    public static class SaveEvent extends MitarbeiterFormEvent {
        SaveEvent(MitarbeiterForm source, Mitarbeiter mitarbeiter) {
            super(source, mitarbeiter);
        }
    }

    public static class DeleteEvent extends MitarbeiterFormEvent {
        DeleteEvent(MitarbeiterForm source, Mitarbeiter mitarbeiter) {
            super(source, mitarbeiter);
        }

    }

    public static class CloseEvent extends MitarbeiterFormEvent {
        CloseEvent(MitarbeiterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
        ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
