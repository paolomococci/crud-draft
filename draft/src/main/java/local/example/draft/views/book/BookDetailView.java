package local.example.draft.views.book;

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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import local.example.draft.data.entity.Book;
import local.example.draft.data.service.BookService;
import local.example.draft.views.MainLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Book Detail")
@Route(value = "book-detail/:bookID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
@Uses(Icon.class)
public class BookDetailView
        extends Div
        implements BeforeEnterObserver {

    private final String BOOK_ID = "bookID";
    private final String BOOK_EDIT_ROUTE_TEMPLATE = "book-detail/%d/edit";

    private Grid<Book> bookGrid = new Grid<>(Book.class, false);

    private TextField title;
    private TextField author;
    private DatePicker publicationDate;
    private TextField pages;
    private TextField isbn;
    private Checkbox availability;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Book> bookBeanValidationBinder;

    private Book book;

    private BookService bookService;

    public BookDetailView(@Autowired BookService bookService) {
        this.bookService = bookService;
        addClassNames("book-detail-view", "flex", "flex-col", "h-full");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        bookGrid.addColumn("title").setAutoWidth(true);
        bookGrid.addColumn("author").setAutoWidth(true);
        bookGrid.addColumn("publicationDate").setAutoWidth(true);
        bookGrid.addColumn("pages").setAutoWidth(true);
        bookGrid.addColumn("isbn").setAutoWidth(true);

        /* TemplateRenderer is deprecated */
        TemplateRenderer<Book> availabilityRenderer = TemplateRenderer.<Book>of(
                """
                    <vaadin-icon 
                        hidden='[[!item.availability]]' 
                        icon='vaadin:check' 
                        style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'>
                    </vaadin-icon>
                    <vaadin-icon 
                        hidden='[[item.availability]]' 
                        icon='vaadin:minus' 
                        style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'>
                    </vaadin-icon>
                """
                ).withProperty(
                "availability",
                Book::isAvailability
        );

        bookGrid.addColumn(availabilityRenderer).setHeader("Availability").setAutoWidth(true);

        bookGrid.setItems(query -> bookService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        bookGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        bookGrid.setHeightFull();

        bookGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(BOOK_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(BookDetailView.class);
            }
        });

        bookBeanValidationBinder = new BeanValidationBinder<>(Book.class);

        bookBeanValidationBinder.forField(pages).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("pages");

        bookBeanValidationBinder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.book == null) {
                    this.book = new Book();
                }
                bookBeanValidationBinder.writeBean(this.book);

                bookService.update(this.book);
                clearForm();
                refreshGrid();
                Notification.show("Book details stored.");
                UI.getCurrent().navigate(BookDetailView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the book details.");
            }
        });
        save.addClickShortcut(Key.ENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> bookId = event.getRouteParameters().getLong(BOOK_ID);
        if (bookId.isPresent()) {
            Optional<Book> bookFromBackend = bookService.get(bookId.get());
            if (bookFromBackend.isPresent()) {
                populateForm(bookFromBackend.get());
            } else {
                Notification.show(String.format("The requested book was not found, ID = %d", bookId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(BookDetailView.class);
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
        title = new TextField("Title");
        author = new TextField("Author");
        publicationDate = new DatePicker("Publication Date");
        pages = new TextField("Pages");
        isbn = new TextField("Isbn");
        availability = new Checkbox("Availability");
        availability.getStyle().set("padding-top", "var(--lumo-space-m)");
        Component[] fields = new Component[]{title, author, publicationDate, pages, isbn, availability};

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
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
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
        wrapper.add(bookGrid);
    }

    private void refreshGrid() {
        bookGrid.select(null);
        bookGrid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Book value) {
        this.book = value;
        bookBeanValidationBinder.readBean(this.book);
    }
}
