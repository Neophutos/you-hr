package com.example.application.views;

import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.about.AboutView;
import com.example.application.views.antrag.AntragVerwaltungView;
import com.example.application.views.antrag.AntragView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.einstellungen.EinstellungenView;
import com.example.application.views.meinedaten.MeineDatenView;
import com.example.application.views.mitarbeiterliste.MitarbeiterlisteView;
import com.example.application.views.rechteverwaltung.RechteverwaltungView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */

/**
 * @desc Der View MainLayout implementiert eine Ansicht für alle angemeldete Mitarbeiter von der sie aus auf andere Views kommen können.
 * Hauptbestandteil stellt hierbei die Implementierung der Menüliste auf der linken Seite des Bildschirmes dar.
 *
 *
 * @category View
 * @version 1.0
 * @since 2022-06-30
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames("menu-item-link");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames("menu-item-text");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                addClassNames("menu-item-icon");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    /**
     *
     * @desc Initialisieren des Logos und der Überschrift der Menüliste
     */

    private Component createDrawerContent() {
        HorizontalLayout top = new HorizontalLayout();
        Image logo = new Image("/icons/YouLogo_Large.png","YOU-Logo");
        logo.setMaxWidth("30px");
        Text title = new Text("YOU - Modern HR");
        top.setAlignItems(FlexComponent.Alignment.CENTER);
        top.add(logo, title);
        H2 appName = new H2(top);

        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    /**
     *
     * @desc Initialisieren der Menüliste
     */
    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("menu-item-container");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("navigation-list");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }
        }
        return nav;
    }

    /**
     *
     * @desc Initialisieren der Interaktionselemente aus der Menüliste
     */

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //

                new MenuItemInfo("Dashboard", "la la-home", DashboardView.class), //

                new MenuItemInfo("Mitarbeiterliste", "la la-address-book", MitarbeiterlisteView.class), //

                new MenuItemInfo("Anträge", "la la-inbox", AntragVerwaltungView.class), //

                new MenuItemInfo("Rechteverwaltung", "la la-cog", RechteverwaltungView.class), //

                new MenuItemInfo("Einstellungen", "la la-save", EinstellungenView.class), //

                new MenuItemInfo("Antrag/Problem erstellen", "la la-paste", AntragView.class), //

                new MenuItemInfo("Über YOU", "la la-info-circle", AboutView.class),

        };
    }

    /**
     *
     * @desc Initialisieren des Feldes, in dem eingeloggter Benutzer und Profilbild angezeigt werden
     */
    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("footer");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Meine Daten", e -> {
                getUI().ifPresent(ui -> ui.navigate(
                        MeineDatenView.class));
            }).addComponentAsFirst(createIcon(VaadinIcon.BOOK));
            userMenu.addItem("Logout", e -> {
                authenticatedUser.logout();
            }).addComponentAsFirst(createIcon(VaadinIcon.SIGN_OUT));

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
