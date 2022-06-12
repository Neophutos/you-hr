package com.example.application.views.problemformular;

import com.example.application.data.entity.Problem;
import com.example.application.data.service.ProblemformularService;
import com.example.application.views.MainLayout;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.personformular.MitarbeiterForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;

@PageTitle("Problemformular")
@Route(value = "Problemformular", layout = MainLayout.class)
@RolesAllowed("USER")
public class ProblemformularView extends Div {
//    private Binder<Problem> problemformularBinder;
    private BeanValidationBinder<Problem> problemformularBinder;

    private ComboBox<String> problemart = new ComboBox<>("Problemart");
    private TextArea beschreibung = new TextArea("Problembeschreibung");

    Button absenden = new Button("Abschicken");
    Button schliessen = new Button("Schließen");

    private ProblemformularService problemformularservice;

    private Problem problem;

    @Autowired
    public ProblemformularView(ProblemformularService problemformularService) {
        this.problemformularservice = problemformularService;
        addClassName("Problem-Formular");

        problemformularBinder = new BeanValidationBinder<>(Problem.class);

        problemformularBinder.bindInstanceFields(this);

        add(createFormLayout(),createButtonLayout());
    }


    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(problemart, beschreibung);
        problemart.setItems("Potenzielle Gefahr", "Bug/Systemfehler", "Profil/Nutzerkonto", "Sonstiges Problem");
        formLayout.setColspan(problemart, 1);
        formLayout.setColspan(beschreibung, 2);
        formLayout.setMaxWidth("700px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();

        absenden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        absenden.addClickListener(event -> checkundSend());
        schliessen.addClickListener(event -> schliessen.getUI().ifPresent(ui ->
                ui.navigate("dashboard")));

        absenden.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        problemformularBinder.addStatusChangeListener(e -> absenden.setEnabled(problemformularBinder.isValid()));

        buttonLayout.add(absenden);
        buttonLayout.add(schliessen);
        return buttonLayout;
    }

    private void checkundSend() {
        try {
            this.problem = new Problem();

            this.problem.setDatum(LocalDate.now());

            problemformularBinder.writeBean(problem);

            problemformularservice.update(problem);

            Notification.show("Ihr Problem wurde erfolgreich gemeldet!");

            UI.getCurrent().navigate(DashboardView.class);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


}
