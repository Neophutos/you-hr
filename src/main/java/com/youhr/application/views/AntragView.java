package com.youhr.application.views;

import com.youhr.application.data.service.AntragService;
import com.youhr.application.forms.AntragForm;
import com.youhr.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;


/**
 * @desc Der View Antrag implementiert die Klasse AntragForm und stellt diese im grafischen Interface des Programms dar.
 *
 * @see AntragForm
 *
 * @category View
 * @version 1.0
 * @since 2022-06-30
 */
@PageTitle("Antrag/Problem einreichen")
@Route(value = "antragsstellung", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
@Uses(Icon.class)
public class AntragView extends Div {

    private AntragService antragService;

    AntragForm form;

    /**
     * @desc Initialisierung des grafischen Interfaces
     * @param antragService
     */
    @Autowired
    public AntragView(AntragService antragService) {
        this.antragService = antragService;
        addClassName("antrag-view");

        configureForm();

        add(getContent());
    }

    /**
     * @desc Konfiguration der Ausrichtung und Form des Formulars AntragForm.
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        content.addClassName("content");
        content.setSizeFull();
        content.setMargin(true);
        return content;
    }

    /**
     * @desc Initialisierung des Formulars mit dem entsprechenden Service zur Kommunikation mit der Datenbank.
     */
    private void configureForm() {
        form = new AntragForm(antragService);
        form.setWidth("25em");
    }
}
