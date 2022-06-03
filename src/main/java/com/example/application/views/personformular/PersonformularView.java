package com.example.application.views.personformular;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@PageTitle("Mitarbeiter erstellen")
@Route(value = "personformular", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class PersonformularView extends Div {

    private TextField firstName = new TextField("Vorname");
    private TextField lastName = new TextField("Nachname");
    private EmailField email = new EmailField("E-Mail");
    private DatePicker dateOfBirth = new DatePicker("Geburtstag");
    private PhoneNumberField phone = new PhoneNumberField("Telefonnummer");
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

    private Binder<SamplePerson> binder = new Binder<>(SamplePerson.class);

    private Tabs tabs = new Tabs();
    private Component currentContent = new Div();

    private Map<Tab, Component> parts = new LinkedHashMap<>();
    private Map<Component, Tab> rootToTab = new HashMap<>();

    public PersonformularView(SamplePersonService personService) {
        addClassName("personformular-view");

        parts.put(new Tab("Information"), new VerticalLayout(firstName, lastName, email, dateOfBirth, phone, position, abteilung));
        parts.put(new Tab("Adresse"), new VerticalLayout(street, streetNumber, postalcode, city, state, country));

        parts.forEach((tab, root) -> rootToTab.put(root, tab));
        parts.keySet().forEach(tabs::add);

        add(tabs, currentContent, new VerticalLayout(save));

        tabs.addSelectedChangeListener(event -> showTab(event.getSelectedTab()));
        showTab(tabs.getSelectedTab());

        //add(createDescription());
        //add(createTitle1());
        //add(createFormLayout1());
        //add(createTitle2());
        //add(createFormLayout2());
        //add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " - Daten gespeichert.");
            clearForm();
        });
    }

    private void showTab(Tab tab) {
        Component newContent = parts.get(tab);
        replace(currentContent, newContent);
        currentContent = newContent;
    }

    private void clearForm() {
        binder.setBean(new SamplePerson());
    }

    private Component createDescription(){
        return new H6("In diesem Formular können Sie neue Mitarbeiter anlegen.");
    }

    private Component createTitle1() {
        return new H3("Persönliche Informationen");
    }

    private Component createFormLayout1() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Bitte geben Sie eine gültige E-Mail ein!");
        formLayout.add(firstName, lastName, dateOfBirth, phone, email, position, abteilung);
        abteilung.setItems("Controlling","Finance","HR","IT","Legal Affairs", "Management", "Operations");

        firstName.setRequired(true);
        lastName.setRequired(true);
        dateOfBirth.setRequired(true);
        abteilung.setRequired(true);

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

        street.setRequired(true);
        streetNumber.setRequired(true);
        postalcode.setRequired(true);
        city.setRequired(true);
        state.setRequired(true);
        country.setRequired(true);

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
