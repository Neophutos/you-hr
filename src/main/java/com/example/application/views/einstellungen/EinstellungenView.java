package com.example.application.views.einstellungen;

import com.example.application.views.MainLayout;
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

}
