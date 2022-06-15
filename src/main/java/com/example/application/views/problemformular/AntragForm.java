package com.example.application.views.problemformular;

import com.example.application.data.entity.Problem;
import com.example.application.data.generator.DataGenerator;
import com.example.application.data.service.ProblemformularService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class AntragForm extends FormLayout {

    private BeanValidationBinder<Problem> problemformularBinder;
    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());

    Text text = new Text("Wählen Sie die Art des Antrags aus!");
    private ComboBox<String> problemart = new ComboBox<>("Problemart");
    private TextArea beschreibung = new TextArea("Problembeschreibung");

    Button absenden = new Button("Abschicken");
    Button schliessen = new Button("Schließen");

    private ProblemformularService problemformularservice;

    private Problem problem;

    @Autowired
    public AntragForm(ProblemformularService problemformularService) {
        this.problemformularservice = problemformularService;
        addClassName("Antrag-Formular");

        problemformularBinder = new BeanValidationBinder<>(Problem.class);

        problemformularBinder.bindInstanceFields(this);

        problemart.setItems("Rechteänderung", "Bug/Systemfehler", "Profil/Nutzerkonto", "Sonstiges Problem");

        add(
                new H6("In diesem Formular können Sie zum HR-System bezogene Anfragen/Probleme melden."),
                problemart,
                beschreibung,
                createButtonLayout()
        );
    }

    private Component createButtonLayout() {
        absenden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        schliessen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        absenden.addClickListener(event -> checkundSend());
        schliessen.addClickListener(event -> schliessen.getUI().ifPresent(ui ->
                ui.navigate("dashboard")));

        absenden.addClickShortcut(Key.ENTER);
        schliessen.addClickShortcut(Key.ESCAPE);

        problemformularBinder.addStatusChangeListener(e -> absenden.setEnabled(problemformularBinder.isValid()));

        return new HorizontalLayout(absenden, schliessen);
    }

    private void checkundSend() {
        try {
            this.problem = new Problem();

            this.problem.setDatum(LocalDate.now());

            this.problem.setAntragstellername(authenticatedUser.get().get().getName());

            problemformularBinder.writeBean(problem);

            problemformularservice.update(problem);

            Notification.show("Ihr Problem wurde erfolgreich gemeldet!");

            UI.getCurrent().navigate(DashboardView.class);

        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
