package com.youhr.application.views;

import com.youhr.application.layout.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;


/**
 * @desc Der View About stellt in mehreren Abschnitten Grundinformationen zum Programm dar. Diese Klasse ist ausschließlich textbasiert und fordert weder Ein-, noch Ausgaben.
 *
 * @category View
 * @author Natalie Stache, Ben Köppe
 * @version 1.0
 * @since 2022-07-04
 */
@PageTitle("Über YOU | YOU-HR")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
public class AboutView extends VerticalLayout {

    Image gitlogo = new Image("/images/github.png", "GitHub Logo");

    private final Button github = new Button(gitlogo);

    /**
     * @desc Initialisierung des grafischen Interfaces
     */
    public AboutView() {
        setMargin(true);

        Accordion beschreibung = new Accordion();

        Span was = new Span("YOU ist ein Prototyp für ein Personalverwaltungssystem, das grundlegende Funktionen für die Nutzung im Personalwesen bereitstellt.");
        AccordionPanel aboutLayout = beschreibung.add("\uD83E\uDD14 Was ist YOU?", was);
        aboutLayout.addThemeVariants(DetailsVariant.REVERSE);

        Span wie = new Span("Das System basiert auf der Sprache Java. Mithilfe der Frameworks Spring, Vaadin und JPA wurden sowohl Database-Anbindungen, als auch das Web-UI erstellt.");
        AccordionPanel howLayout = beschreibung.add("\uD83D\uDDC3 Wie funktioniert das System?", wie);
        howLayout.addThemeVariants(DetailsVariant.REVERSE);

        Span team = new Span("Erstellt vom Team TheOptimisticHR:");

        Span ben = new Span("- Ben Köppe (Developer)");
        Span riccardo = new Span("- Riccardo Prochnow");
        Span tim = new Span("- Tim Freund");
        Span chris = new Span("- Chris Zobel");
        Span natalie = new Span("- Natalie Stache");

        VerticalLayout TeamInformationLayout = new VerticalLayout(team, ben, tim, riccardo, chris, natalie);

        TeamInformationLayout.setSpacing(false);
        TeamInformationLayout.setPadding(false);

        AccordionPanel whoLayout = beschreibung.add("\uD83D\uDC68\u200D\uD83D\uDCBB Wer entwickelt YOU?", TeamInformationLayout);
        whoLayout.addThemeVariants(DetailsVariant.REVERSE);

        github.addClickListener(spanClickEvent -> github.getUI().ifPresent(ui -> ui.getPage().setLocation(
                "https://github.com/Neophutos/theoptimistic-hr")));
        github.addThemeVariants(ButtonVariant.LUMO_ICON);
        gitlogo.setMaxWidth("30px");

        Span sourcecode = new Span("Unseren Quellcode finden Sie unter folgendem Link:");
        VerticalLayout CodeInformationLayout = new VerticalLayout(sourcecode, github);
        CodeInformationLayout.setSpacing(false);
        CodeInformationLayout.setPadding(false);

        AccordionPanel codeLayout = beschreibung.add("\uD83D\uDEE0 Wo finde ich den Source-Code?", CodeInformationLayout);
        codeLayout.addThemeVariants(DetailsVariant.REVERSE);

        beschreibung.setMaxWidth("450px");
        beschreibung.setMinWidth("450px");

        add(beschreibung);
    }
}
