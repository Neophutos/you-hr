package com.example.application.views.mitarbeiterakte;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

@PageTitle("Mitarbeiterakte")
@Route(value = "mitarbeiterakte", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class MitarbeiterakteView extends VerticalLayout {

    public MitarbeiterakteView() {

    }

}
