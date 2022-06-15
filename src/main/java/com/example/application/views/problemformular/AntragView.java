package com.example.application.views.problemformular;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problem;
import com.example.application.data.generator.DataGenerator;
import com.example.application.data.repository.UserRepository;
import com.example.application.data.service.ProblemformularService;
import com.example.application.data.service.UserService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.personformular.MitarbeiterForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
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

@PageTitle("Problemformular")
@Route(value = "problemformular", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AntragView extends Div {

    private ProblemformularService problemformularservice;

    AntragForm form;

    @Autowired
    public AntragView(ProblemformularService problemformularService) {
        this.problemformularservice = problemformularService;
        addClassName("antrag-view");

        configureForm();

        add(getContent());

    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        content.addClassName("content");
        content.setSizeFull();
        content.setMargin(true);
        return content;
    }

    private void configureForm() {
        form = new AntragForm(problemformularservice);
        form.setWidth("25em");
    }
}
