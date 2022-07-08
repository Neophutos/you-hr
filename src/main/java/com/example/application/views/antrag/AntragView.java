package com.example.application.views.antrag;

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


/**
 * Der View Antrag implementiert die Klasse AntragForm und stellt diese
 * im grafischen Interface des Programms dar.
 *
 * @see AntragForm
 *
 * @author Ben Köppe / Neophutos
 * @version 1.0
 * @since 2022-06-30
 */
@PageTitle("Antrag/Problem einreichen")
@Route(value = "antragsstellung", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class AntragView extends Div {

    private AntragService antragservice;

    AntragForm form;

    /**
     * Diese Methode initialisiert das grafische Interface
     * und fügt diese zum View hinzu.
     *
     * @param antragservice
     */
    @Autowired
    public AntragView(AntragService antragservice) {
        this.antragservice = antragservice;
        addClassName("antrag-view");

        configureForm();

        add(getContent());
    }

    /**
     * Diese Component-Methode konfiguriert die Ausrichtung
     * und Form des Formulars AntragForm.
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        content.addClassName("content");
        content.setSizeFull();
        content.setMargin(true);
        return content;
    }

    /**
     * Diese Methode initialisiert das Formular mit dem
     * entsprechenden Service zur Kommunikation mit
     * der Datenbank.
     */
    private void configureForm() {
        form = new AntragForm(antragservice);
        form.setWidth("25em");
    }
}
