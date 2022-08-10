package com.youhr.application.views;

import com.youhr.application.views.MainLayout;
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
 * @version 1.0
 * @since 2022-07-04
 */
@PageTitle("Über YOU")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
public class AboutView extends VerticalLayout {

    private final Accordion beschreibung = new Accordion();

    private final Span was = new Span("YOU ist ein Prototyp für ein Personalverwaltungssystem, das grundlegende Funktionen für die Nutzung im Personalwesen bereitstellt.");

    private final Span wie = new Span("Das System basiert auf der Sprache Java. Mithilfe der Frameworks Spring, Vaadin und JPA wurden sowohl Database-Anbindungen, als auch das Web-UI erstellt.");

    private final Span team = new Span("Erstellt vom Team TheOptimisticHR:");
    private final Span ben = new Span("- Ben Köppe (Developer)");
    private final Span tim = new Span("- Tim Freund");
    private final Span riccardo = new Span("- Riccardo Prochnow");
    private final Span chris = new Span("- Chris Zobel");
    private final Span natalie = new Span("- Natalie Stache");

    Image gitlogo = new Image("/images/github.png", "GitHub Logo");

    private final Span sourcecode = new Span("Unseren Quellcode finden Sie unter folgendem Link:");
    private final Button github = new Button(gitlogo);

    /**
     * @desc Initialisierung des grafischen Interfaces
     */
    public AboutView() {
        setMargin(true);

        AccordionPanel aboutLayout = beschreibung.add("\uD83E\uDD14 Was ist YOU?", was);
        aboutLayout.addThemeVariants(DetailsVariant.REVERSE);

        AccordionPanel howLayout = beschreibung.add("\uD83D\uDDC3 Wie funktioniert das System?", wie);
        howLayout.addThemeVariants(DetailsVariant.REVERSE);

        VerticalLayout TeamInformationLayout = new VerticalLayout(team, ben, tim, riccardo, chris, natalie);
        TeamInformationLayout.setSpacing(false);
        TeamInformationLayout.setPadding(false);

        AccordionPanel whoLayout = beschreibung.add("\uD83D\uDC68\u200D\uD83D\uDCBB Wer entwickelt YOU?", TeamInformationLayout);
        whoLayout.addThemeVariants(DetailsVariant.REVERSE);

        github.addClickListener(spanClickEvent -> github.getUI().ifPresent(ui -> ui.getPage().setLocation(
                "https://github.com/Neophutos/theoptimistic-hr")));
        github.addThemeVariants(ButtonVariant.LUMO_ICON);
        gitlogo.setMaxWidth("30px");

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
