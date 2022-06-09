package com.example.application.views.problemmanagement;

import com.example.application.data.entity.Problem;
import com.example.application.data.repository.ProblemformularRepository;
import com.example.application.data.service.ProblemformularService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Problem-Management")
@Route(value = "problem-management", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ProblemManagementView extends Div {

    private ProblemformularService problemformularService;

    private List<Problem> probleme = problemformularService.findAllProblems();

    private ComponentRenderer<Component, Problem> problemCardRenderer = new ComponentRenderer<>(problem -> {
        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.setMargin(true);

        Avatar avatar = new Avatar(problem.getProblemart(), getPicture(problem));
        avatar.setHeight("64px");
        avatar.setWidth("64px");

        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);
        infoLayout.getElement().appendChild(ElementFactory.createStrong(problem.getProblemart()));

        VerticalLayout contactLayout = new VerticalLayout();
        contactLayout.setSpacing(false);
        contactLayout.setPadding(false);
        contactLayout.add(new Div(new Text(problem.getBeschreibung())));
        infoLayout.add(new Details("Problembeschreibung", contactLayout));

        cardLayout.add(avatar, infoLayout);
        return cardLayout;
    });

    private String getPicture(Problem problem) {
        String url = "";
        switch (problem.getProblemart()) {
            case "Potenzielle Gefahr":
                url = "/resources/icons/warning-sign.png";
                break;
            case "Bug/Systemfehler":
                url = "/resources/icons/bug.png";
                break;
            case "Profil/Nutzerkonto":
                url = "/resources/icons/user.png";
                break;
            case "Sonstiges Problem":
                url = "/resources/icons/adware.png";
                break;
        }
        return url;
    }

    public ProblemManagementView() {
        VirtualList<Problem> list = new VirtualList<>();
        list.setItems(probleme);
        list.setRenderer(problemCardRenderer);
        add(list);
    }

}
