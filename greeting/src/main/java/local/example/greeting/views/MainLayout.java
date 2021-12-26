package local.example.greeting.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import local.example.greeting.data.entity.User;
import local.example.greeting.security.AuthenticatedUser;
import local.example.greeting.views.greeting.GreetingView;
import local.example.greeting.views.guests.GuestsView;
import local.example.greeting.views.home.HomeView;

@PageTitle("Main")
public class MainLayout
        extends AppLayout {

    public record MenuItemInfo(
            String text,
            String iconClass,
            Class<? extends Component> view
    ) {

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(
            AuthenticatedUser authenticatedUser,
            AccessAnnotationChecker accessChecker
    ) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(
                "bg-base",
                "border-b",
                "border-contrast-10",
                "box-border",
                "flex",
                "flex-col",
                "w-full"
        );

        Div layout = new Div();
        layout.addClassNames(
                "flex",
                "h-xl",
                "items-center",
                "px-l"
        );

        H1 appName = new H1("Greeting");
        appName.addClassNames(
                "my-0",
                "me-auto",
                "text-l"
        );
        layout.add(appName);

        Optional<User> maybeUser = authenticatedUser.get();

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(
                    user.getName(),
                    user.getProfilePictureUrl()
            );
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem(
                    "Logout",
                    e -> authenticatedUser.logout()
            );

            Span name = new Span(user.getName());
            name.addClassNames(
                    "font-medium",
                    "text-s",
                    "text-secondary"
            );

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor(
                    "login",
                    "Login"
            );
            layout.add(loginLink);
        }

        Nav nav = new Nav();
        nav.addClassNames(
                "flex",
                "gap-s",
                "overflow-auto",
                "px-m"
        );

        UnorderedList list = new UnorderedList();
        list.addClassNames(
                "flex",
                "paginate-none",
                "m-0",
                "p-0"
        );
        nav.add(list);

        for (RouterLink link : createLinks()) {
            ListItem item = new ListItem(link);
            list.add(item);
        }

        header.add(layout, nav);
        return header;
    }

    private List<RouterLink> createLinks() {
        MenuItemInfo[] menuItems = new MenuItemInfo[] {
                new MenuItemInfo(
                        "Home",
                        "la la-home",
                        HomeView.class
                ),
                new MenuItemInfo(
                        "Greeting",
                        "la la-handshake",
                        GreetingView.class
                ),
                new MenuItemInfo(
                        "Guests",
                        "la la-users-cog",
                        GuestsView.class
                ),
        };
        List<RouterLink> links = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            if (accessChecker.hasAccess(menuItemInfo.getView())) {
                links.add(createLink(menuItemInfo));
            }

        }
        return links;
    }

    private static RouterLink createLink(MenuItemInfo menuItemInfo) {
        RouterLink link = new RouterLink();
        link.addClassNames(
                "flex",
                "h-m",
                "items-center",
                "px-s",
                "relative",
                "text-secondary"
        );
        link.setRoute(menuItemInfo.getView());

        Span icon = new Span();
        icon.addClassNames(
                "me-s", "text-l"
        );
        if (!menuItemInfo.getIconClass().isEmpty()) {
            icon.addClassNames(menuItemInfo.getIconClass());
        }

        Span text = new Span(menuItemInfo.getText());
        text.addClassNames(
                "font-medium",
                "text-s",
                "whitespace-nowrap"
        );

        link.add(icon, text);
        return link;
    }
}
