package com.example.application.views.dashboard;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.generator.DataGenerator;
import com.example.application.data.service.AntragService;
import com.example.application.data.service.MitarbeiterService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RolesAllowed("USER")
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());
    private Text name;
    private Mitarbeiter mitarbeiter;
    private MitarbeiterService mitarbeiterService;
    private AntragService antragService;

    public DashboardView(MitarbeiterService mitarbeiterService, AntragService antragService) {

        if(authenticatedUser.get().get().getMitarbeiter() != null){
            this.mitarbeiter = authenticatedUser.get().get().getMitarbeiter();
            name = new Text("Willkommen zurück " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname() + "! \uD83D\uDC4B");
        } else {
            name = new Text("Willkomen zurück bei YOU! \uD83D\uDC4B");
        }

        Image logo = new Image("/icons/YouLogo.png","YOU-Logo");
        logo.setMaxWidth("100px");
        H3 welcome = new H3(name);
        Paragraph anzahlMitarbeiter = new Paragraph("\uD83D\uDC68\u200D\uD83D\uDCBC Es befinden sich derzeit " + mitarbeiterService.countMitarbeiter() + " Mitarbeiter im Unternehmen");
        Paragraph anzahlAntraege = new Paragraph("\uD83D\uDCD1 In YOU befinden sich derzeit " + antragService.countProblems() + " zu bearbeitende Anträge");
        Paragraph tasks = new Paragraph("\uD83D\uDC81\u200D Nutze das Menü auf der linken Seite, um die Funktionen von YOU zu nutzen!");

        setAlignItems(Alignment.CENTER);
        add(logo, welcome, anzahlMitarbeiter, anzahlAntraege, tasks);
    }
}
