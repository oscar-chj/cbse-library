package com.upm.library.presentation;

import com.upm.library.business.BookService;
import com.upm.library.persistence.Book;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Main Vaadin UI view for the Library Catalog Management System.
 * Provides the interface for librarians to manage book records.
 *
 * This is the Presentation Component in the CBSE layered architecture.
 *
 * Implements:
 * - UC-1: Add New Book (form + submit button)
 * - UC-2: View All Books (data grid)
 * - UC-3: Search Book by Title (search field with live filtering)
 * - UC-4: Delete Book Record (delete button per row)
 */
@Route("")
@PageTitle("Library Catalog")
public class BookView extends VerticalLayout {

    private final BookService bookService;
    private final Grid<Book> grid = new Grid<>(Book.class, false);
    private final TextField searchField = new TextField();
    private final TextField titleField = new TextField("Title");
    private final TextField authorField = new TextField("Author");
    private final TextField isbnField = new TextField("ISBN");

    /**
     * Constructs the BookView with all UI components.
     *
     * @param bookService the business service component (injected by Spring)
     */
    public BookView(BookService bookService) {
        this.bookService = bookService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // --- Header ---
        H1 header = new H1("\uD83D\uDCDA Library Catalog");

        // --- Search Field ---
        searchField.setPlaceholder("Search by title...");
        searchField.setClearButtonVisible(true);
        searchField.setWidthFull();
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> updateGrid());

        // --- Data Grid ---
        configureGrid();

        // --- Add Book Form ---
        H3 formTitle = new H3("Add New Book");
        titleField.setPlaceholder("Enter book title");
        authorField.setPlaceholder("Enter author name");
        isbnField.setPlaceholder("Enter ISBN");

        Button addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> addBook());

        HorizontalLayout formLayout = new HorizontalLayout(
                titleField, authorField, isbnField, addButton
        );
        formLayout.setWidthFull();
        formLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        formLayout.expand(titleField, authorField, isbnField);

        // --- Assemble Layout ---
        add(header, searchField, grid, formTitle, formLayout);

        // --- Load Initial Data (UC-2) ---
        updateGrid();
    }

    /**
     * Configures the data grid columns.
     */
    private void configureGrid() {
        grid.addColumn(Book::getTitle).setHeader("Title").setSortable(true).setAutoWidth(true);
        grid.addColumn(Book::getAuthor).setHeader("Author").setSortable(true).setAutoWidth(true);
        grid.addColumn(Book::getIsbn).setHeader("ISBN").setAutoWidth(true);
        grid.addComponentColumn(book -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            deleteButton.addClickListener(e -> deleteBook(book));
            return deleteButton;
        }).setHeader("Actions").setAutoWidth(true);

        grid.setWidthFull();
        grid.setHeight("400px");
    }

    /**
     * Updates the grid based on the current search filter.
     * Implements UC-2 (View All) and UC-3 (Search by Title).
     */
    private void updateGrid() {
        String filter = searchField.getValue();
        if (filter == null || filter.trim().isEmpty()) {
            grid.setItems(bookService.findAll());
        } else {
            grid.setItems(bookService.findByTitle(filter));
        }
    }

    /**
     * Adds a new book from the form fields.
     * Implements UC-1 (Add New Book).
     */
    private void addBook() {
        try {
            Book book = new Book(
                    titleField.getValue(),
                    authorField.getValue(),
                    isbnField.getValue()
            );
            bookService.save(book);

            // Show success notification
            Notification.show("Book added successfully: " + book.getTitle(),
                            3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Clear form fields
            titleField.clear();
            authorField.clear();
            isbnField.clear();

            // Refresh grid
            updateGrid();
        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage(),
                            4000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Deletes a book and refreshes the grid.
     * Implements UC-4 (Delete Book Record).
     *
     * @param book the book to delete
     */
    private void deleteBook(Book book) {
        bookService.deleteById(book.getId());
        Notification.show("Book deleted: " + book.getTitle(),
                        3000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        updateGrid();
    }
}
