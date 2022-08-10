package com.youhr.application.views;

import com.youhr.application.data.objekt.Mitarbeiter;
import com.youhr.application.data.generator.DataGenerator;
import com.youhr.application.data.service.AntragService;
import com.youhr.application.data.service.MitarbeiterService;
import com.youhr.application.security.AuthenticatedUser;
import com.youhr.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.RolesAllowed;

/**
 * @desc Der View Dashboard stellt die Willkommensseite des Programms dar. Der Nutzer wird mit eigenem Namen gegrüßt und ein paar Fakten zum Personalwesen werden dargestellt (für die User-Experience).
 *
 * @category View
 * @version 0.7
 * @since 2022-07-05
 */
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"MITARBEITER","PERSONALER","ADMIN"})
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(DataGenerator.getUserRepository());
    private Text name;
    private Mitarbeiter mitarbeiter;
    private MitarbeiterService mitarbeiterService;
    private AntragService antragService;

    /**
     * @desc Initialisierung des grafischen Interfaces. Dabei werden auf Datenbank-Informationen über die Objekte Mitarbeiter und Antrag zugegriffen.
     * @param mitarbeiterService
     * @param antragService
     */
    public DashboardView(MitarbeiterService mitarbeiterService, AntragService antragService) {

        if(authenticatedUser.get().get().getMitarbeiter() != null){
            this.mitarbeiter = authenticatedUser.get().get().getMitarbeiter();
            name = new Text("Willkommen zurück " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname() + "! \uD83D\uDC4B");
        } else {
            name = new Text("Willkommen zurück bei YOU! \uD83D\uDC4B");
        }

        Image logo = new Image("/icons/YouLogo_Large.png","YOU-Logo");
        logo.setMaxWidth("100px");
        H3 welcome = new H3(name);

        Paragraph anzahlMitarbeiter = new Paragraph("\uD83D\uDC68\u200D\uD83D\uDCBC Es befinden sich derzeit " + mitarbeiterService.countMitarbeiter() + " Mitarbeiter im Unternehmen");
        Paragraph anzahlAntraege = new Paragraph("\uD83D\uDCD1 In YOU befinden sich derzeit " + antragService.countProblems() + " zu bearbeitende Anträge");
        Paragraph anzahlGruppen = new Paragraph("\uD83D\uDC54 Ihr Unternehmen hat derzeit " + mitarbeiterService.findAllAbteilungen().size() + " Abteilungen und " + mitarbeiterService.findAllTeams().size() + " Teams");
        Paragraph tasks = new Paragraph("\uD83D\uDC81\u200D Nutze das Menü auf der linken Seite, um die Funktionen von YOU zu nutzen!");

        H5 berechtigungen = new H5("Du besitzt die Berechtigungen " + authenticatedUser.get().get().getRoles());

        setAlignItems(Alignment.CENTER);
        add(logo, welcome, anzahlMitarbeiter, anzahlGruppen, anzahlAntraege, tasks, berechtigungen);
    }
}
