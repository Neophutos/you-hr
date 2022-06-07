package com.example.application.views.personformular;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.MainLayout;
import com.example.application.views.mitarbeiterliste.MitarbeiterlisteView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@PageTitle("Personformular")
@Route(value = "personformular", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class PersonformularView extends Div {

    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");
    private EmailField email = new EmailField("E-Mail");
    private TextField geburtsdatum = new TextField("Geburtstag");
    private PhoneNumberField telefonnr = new PhoneNumberField("Telefonnummer");
    private TextField position = new TextField("Position");
    private ComboBox<String> abteilung = new ComboBox<>("Abteilung");
    private TextField street = new TextField("Straße");
    private TextField streetNumber = new TextField("Hausnummer");
    private TextField postalcode = new TextField("Postleitzahl");
    private TextField city = new TextField("Stadt");
    private ComboBox<String> state = new ComboBox<>("Bundesland");
    private ComboBox<String> country = new ComboBox<>("Land");

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");

    private BeanValidationBinder<Mitarbeiter> binder;

    private Mitarbeiter mitarbeiter;

    private final MitarbeiterService mitarbeiterService;

    @Autowired
    public PersonformularView(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        addClassNames("personformular-view");

        add(createTitle1());
        add(createFormLayout1());
        add(createTitle2());
        add(createFormLayout2());
        add(createButtonLayout());

        binder = new BeanValidationBinder<>(Mitarbeiter.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> clearForm());

        save.addClickListener(e -> {
           try {
               this.mitarbeiter = new Mitarbeiter();

               binder.writeBean(this.mitarbeiter);

               mitarbeiterService.update(this.mitarbeiter);
               clearForm();
               UI.getCurrent().navigate(MitarbeiterlisteView.class);
               Notification.show("Daten gespeichert.");
           } catch (ValidationException validationException){
               Notification.show("Eine Exception ist während der Speicherung aufgetreten.");
           }
        });
    }

    private void clearForm() {
        binder.setBean(new Mitarbeiter());
    }

    private Component createTitle1() {
        return new H3("Persönliche Informationen");
    }

    private Component createFormLayout1() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Bitte geben Sie eine gültige E-Mail ein!");
        formLayout.add(vorname, nachname, geburtsdatum, telefonnr, email, position, abteilung);
        abteilung.setItems("Controlling","Finance","HR","IT","Legal Affairs", "Management", "Operations");
        return formLayout;
    }

    private Component createTitle2() {
        return new H3("Adresse");
    }

    private Component createFormLayout2() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Bitte geben Sie eine gültige E-Mail ein!");
        formLayout.add(street, streetNumber, postalcode, city, state, country);
        country.setItems("Deutschland", "Österreich", "Schweiz", "Atlantis");
        state.setItems("Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen");
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Land");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+41", "+42", "+43", "+44", "+45", "+46", "+47", "+48", "+49", "+50");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
