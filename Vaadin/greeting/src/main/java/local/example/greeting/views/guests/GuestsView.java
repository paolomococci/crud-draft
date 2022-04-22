package local.example.greeting.views.guests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.Optional;
import javax.annotation.security.RolesAllowed;

import local.example.greeting.data.entity.Guest;
import local.example.greeting.data.service.GuestService;
import local.example.greeting.views.MainLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Guests")
@Route(value = "guests/:guestID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
@Uses(Icon.class)
public class GuestsView
        extends Div
        implements BeforeEnterObserver {

    @Autowired
    GuestService guestService;

    private final String GUEST_EDIT_ROUTE_TEMPLATE = "guests/%d/edit";

    private final Grid<Guest> guestGrid = new Grid<>(
            Guest.class,
            false
    );

    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField phone;
    private DatePicker birthday;
    private TextField occupation;
    private Checkbox acknowledged;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Guest> guestBeanValidationBinder;

    private Guest guest;

    public GuestsView() {

        addClassNames("guests-view", "flex", "flex-col", "h-full");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        guestGrid.addColumn("name").setAutoWidth(true);
        guestGrid.addColumn("surname").setAutoWidth(true);
        guestGrid.addColumn("email").setAutoWidth(true);
        guestGrid.addColumn("phone").setAutoWidth(true);
        guestGrid.addColumn("dateOfBirth").setAutoWidth(true);
        guestGrid.addColumn("occupation").setAutoWidth(true);

        /* TemplateRenderer is deprecated */
        TemplateRenderer<Guest> acknowledgedRenderer = TemplateRenderer
                .<Guest>of(
                        """
                            <vaadin-icon 
                                hidden='[[!item.acknowledged]]' 
                                icon='vaadin:check' 
                                style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'>
                            </vaadin-icon>
                            <vaadin-icon 
                                hidden='[[item.acknowledged]]' 
                                icon='vaadin:minus' 
                                style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'>
                            </vaadin-icon>
                        """
                ).withProperty(
                        "acknowledged",
                        Guest::isAcknowledged
                );

        guestGrid.addColumn(acknowledgedRenderer).setHeader("Acknowledged").setAutoWidth(true);

        guestGrid.setItems(query -> guestService.paginate(PageRequest.of(
                query.getPage(),
                query.getPageSize(),
                VaadinSpringDataHelpers.toSpringDataSort(query)
        )).stream());
        guestGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        guestGrid.setHeightFull();

        guestGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(
                        String.format(GUEST_EDIT_ROUTE_TEMPLATE, event.getValue().getId())
                );
            } else {
                clearForm();
                UI.getCurrent().navigate(GuestsView.class);
            }
        });

        guestBeanValidationBinder = new BeanValidationBinder<>(Guest.class);

        guestBeanValidationBinder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.guest == null) {
                    this.guest = new Guest();
                }
                guestBeanValidationBinder.writeBean(this.guest);

                guestService.update(this.guest);
                clearForm();
                refreshGrid();
                Notification.show("Guest details stored.");
                UI.getCurrent().navigate(GuestsView.class);

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the guest details!");
            }
        });
        save.addClickShortcut(Key.ENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String GUEST_ID = "guestID";
        Optional<Integer> guestId = event.getRouteParameters().getInteger(GUEST_ID);
        if (guestId.isPresent()) {
            Optional<Guest> guestFromBackend = guestService.read(guestId.get());
            if (guestFromBackend.isPresent()) {
                populateForm(guestFromBackend.get());
            } else {
                Notification.show(String.format("The requested guest was not found, ID = %d", guestId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(GuestsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        name = new TextField("Name");
        surname = new TextField("Surname");
        email = new TextField("Email");
        phone = new TextField("Phone");
        birthday = new DatePicker("Birthday");
        occupation = new TextField("Occupation");
        acknowledged = new Checkbox("Acknowledged");
        acknowledged.getStyle().set(
                "padding-top",
                "var(--lumo-space-m)"
        );

        Component[] fields = new Component[] {
                name,
                surname,
                email,
                phone,
                birthday,
                occupation,
                acknowledged
        };

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName(
                "w-full flex-wrap bg-contrast-5 py-s px-l"
        );
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(guestGrid);
    }

    private void refreshGrid() {
        guestGrid.select(null);
        guestGrid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Guest value) {
        this.guest = value;
        guestBeanValidationBinder.readBean(this.guest);
    }
}
