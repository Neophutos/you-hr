package com.example.application.views.problemformular;

import com.example.application.data.service.AntragService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@PageTitle("Problemformular")
@Route(value = "problemformular", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AntragView extends Div {

    private AntragService antragservice;

    AntragForm form;

    @Autowired
    public AntragView(AntragService antragservice) {
        this.antragservice = antragservice;
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
        form = new AntragForm(antragservice);
        form.setWidth("25em");
    }
}
