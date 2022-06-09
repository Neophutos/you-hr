package com.example.application.views.problemformular;

import com.example.application.data.entity.Problemformular;
import com.example.application.data.service.ProblemformularService;
import com.example.application.views.MainLayout;
import com.example.application.views.personformular.MitarbeiterForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.text.Normalizer;

@PageTitle("Problemformular")
@Route(value = "Problemformular", layout = MainLayout.class)
@RolesAllowed("USER")
public class ProblemformularView extends Div {
    private Binder<Problemformular> problemformularBinder;


    private ComboBox<String> problemart = new ComboBox<>("Problemart");
    private TextArea beschreibung = new TextArea("Problembeschreibung");


    Button absenden = new Button("Abschicken");
    Button schliessen = new Button("Schließen");



    private final ProblemformularService problemformularservice;


    @Autowired
    public ProblemformularView(ProblemformularService problemformularService) {
        this.problemformularservice = problemformularService;
        addClassName("Problem-Formular");


        problemformularBinder = new BeanValidationBinder<>(Problemformular.class);
        problemformularBinder.bindInstanceFields(this);



        add(createFormLayout(),
                createButtonLayout());


    }


    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(problemart, beschreibung);
        problemart.setItems("IT-Sicherheit", "Bug/Systemfehler", "Passwort vergessen", "Sonstige Probleme");
        formLayout.setColspan(problemart, 1);
        formLayout.setColspan(beschreibung, 2);

        formLayout.setMaxWidth("700px");
        formLayout.setResponsiveSteps(
                // Use one column by default

                new FormLayout.ResponsiveStep("0", 2)
                // Use two columns, if layout's width exceeds 500px

        );

        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        absenden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(absenden);
        buttonLayout.add(schliessen);
        return buttonLayout;
    }



}
