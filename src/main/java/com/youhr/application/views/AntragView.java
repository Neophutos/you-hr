package com.youhr.application.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.youhr.application.data.service.AntragService;
import com.youhr.application.forms.AntragForm;
import com.youhr.application.layout.MainLayout;

import javax.annotation.security.RolesAllowed;


/**
 * @desc Der View Antrag implementiert die Klasse AntragForm und stellt diese im grafischen Interface des Programms dar.
 *
 * @see AntragForm
 *
 * @category View
 * @Author Chris Zobel, Natalie Stache
 * @version 1.0
 * @since 2022-07-30
 */
@PageTitle("Antragsstellung | YOU-HR")
@Route(value = "antragsstellung", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
@Uses(Icon.class)
public class AntragView extends Div {

    private final AntragService antragService;

    AntragForm form;

    /**
     * @desc Initialisierung des grafischen Interfaces
     * @param antragService
     */
    public AntragView(AntragService antragService) {
        this.antragService = antragService;
        addClassName("antrag-view");

        configureForm();

        add(getContent());
    }

    /**
     * @desc Konfiguration der Ausrichtung und Form des Formulars AntragForm.
     */
    private VerticalLayout getContent() {
        VerticalLayout content = new VerticalLayout(getInfoAccordion(), form);
        content.addClassName("content");
        content.setSizeFull();
        content.setMargin(true);
        return content;
    }

    /**
     * @desc Initialisierung des Formulars mit dem entsprechenden Service zur Kommunikation mit der Datenbank.
     */
    private void configureForm() {
        form = new AntragForm(antragService, antragService.findAllStatuses());
        form.setWidth("25em");
    }

    /**
     * @desc Erstellung eines Akkordions, das den Nutzer über die richtige Verwendung des Antragformulars aufklärt.
     */
    private Accordion getInfoAccordion(){
        Accordion infoAccordion = new Accordion();

        Span desc = new Span("In diesem Formular können Sie Anträge zu folgenden Anliegen stellen:");
        Span type1 = new Span("→ Änderung der persönlichen Daten");
        Span type2 = new Span("→ Änderung der Berechtigungen im System (muss von Beauftragten bestätigt worden sein)");
        Span type3 = new Span("→ Meldung systemrelevanter Probleme");

        VerticalLayout infoAccordionLayout = new VerticalLayout(desc, type1, type2, type3);
        infoAccordionLayout.setSpacing(false);
        infoAccordionLayout.setPadding(false);

        infoAccordion.add("Wozu dient dieses Formular?", infoAccordionLayout);

        return infoAccordion;
    }
}
