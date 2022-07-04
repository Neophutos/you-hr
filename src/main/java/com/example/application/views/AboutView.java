package com.example.application.views;

import com.example.application.views.problemformular.AntragView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import javax.annotation.security.RolesAllowed;

@PageTitle("Über YOU")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed("USER")
public class AboutView extends VerticalLayout {

    private Accordion beschreibung = new Accordion();

    private Span was = new Span("YOU ist ein Prototyp für ein Personalverwaltungssystem, das grundlegende Funktionen für die Nutzung im Personalwesen bereitstellt.");

    private Span wie = new Span("Das System basiert auf der Sprache Java. Mithilfe der Frameworks Spring, Vaadin und JPA wurden sowohl Database-Anbindungen, als auch das Web-UI erstellt.");

    private Span team = new Span("Erstellt vom Team TheOptimisticHR:");
    private Span ben = new Span("- Ben Köppe (Developer)");
    private Span tim = new Span("- Tim Freund");
    private Span riccardo = new Span("- Riccardo Prochnow");
    private Span chris = new Span("- Chris Zobel");
    private Span natalie = new Span("- Natalie Stache");

    Image gitlogo = new Image("/images/github.png", "GitHub Logo");

    private Span sourcecode = new Span("Unseren Quellcode finden Sie unter folgendem Link:");
    private Button github = new Button(gitlogo);

    public AboutView() {
        AccordionPanel aboutLayout = beschreibung.add("\uD83E\uDD14 Was ist YOU?", was);
        aboutLayout.addThemeVariants(DetailsVariant.FILLED);

        AccordionPanel howLayout = beschreibung.add("\uD83D\uDDC3 Wie funktioniert das System?", wie);
        howLayout.addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout TeamInformationLayout = new VerticalLayout(team, ben, tim, riccardo, chris, natalie);
        TeamInformationLayout.setSpacing(false);
        TeamInformationLayout.setPadding(false);

        AccordionPanel whoLayout = beschreibung.add("\uD83D\uDC68\u200D\uD83D\uDCBB Wer entwickelt YOU?", TeamInformationLayout);
        whoLayout.addThemeVariants(DetailsVariant.FILLED);

        github.addClickListener(spanClickEvent -> github.getUI().ifPresent(ui -> ui.getPage().setLocation(
                "https://github.com/Neophutos/theoptimistic-hr")));
        github.addThemeVariants(ButtonVariant.LUMO_ICON);
        gitlogo.setMaxWidth("30px");

        VerticalLayout CodeInformationLayout = new VerticalLayout(sourcecode, github);
        CodeInformationLayout.setSpacing(false);
        CodeInformationLayout.setPadding(false);

        AccordionPanel codeLayout = beschreibung.add("\uD83D\uDEE0 Wo finde ich den Source-Code?", CodeInformationLayout);
        codeLayout.addThemeVariants(DetailsVariant.FILLED);

        beschreibung.setMaxWidth("450px");
        beschreibung.setMinWidth("450px");

        add(beschreibung);
    }
}
