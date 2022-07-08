package com.example.application.views.einstellungen;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

/**
 * @desc Im View Einstellungen können sowohl Admins, als auch Personaler Abteilungen und Teams hinzufügen oder entfernen, falls dies nötig ist.
 *
 * @category View
 * @version 0.0
 * @since 2022-00-00
 */
@PageTitle("Einstellungen")
@Route(value = "einstellungen", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EinstellungenView extends VerticalLayout {

    /**
     * @desc Initialisierung des grafischen Interfaces
     */
    public EinstellungenView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        add(new H2("This place intentionally left empty"));
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
