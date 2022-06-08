package com.example.application.views.personformular;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.HRService;
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

    private BeanValidationBinder<Mitarbeiter> binder;

    private Mitarbeiter mitarbeiter;

    private final MitarbeiterService mitarbeiterService;

    HRService service;

    MitarbeiterForm form;

    @Autowired
    public PersonformularView(HRService service, MitarbeiterService mitarbeiterService) {
        this.service = service;
        this.mitarbeiterService = mitarbeiterService;

        addClassNames("personformular-view");

        configureForm();

        add(
            getContent());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new MitarbeiterForm(mitarbeiterService);
        form.setWidth("25em");
    }

    private void clearForm() {
        binder.setBean(new Mitarbeiter());
    }
}
