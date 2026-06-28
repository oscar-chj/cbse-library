package my.upm.library.presentation;

import my.upm.library.business.BookServiceBean;
import my.upm.library.persistence.Book;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class BookController implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private BookServiceBean bookService;

    private List<Book> books;
    private Book newBook;
    private String searchQuery;

    @PostConstruct
    public void init() {
        refreshBooks();
        newBook = new Book();
    }

    public void refreshBooks() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            books = bookService.findAll();
        } else {
            books = bookService.findByTitle(searchQuery.trim());
        }
    }

    public void saveBook() {
        try {
            // Check for duplicate ISBN
            Book existingBook = bookService.findByIsbn(newBook.getIsbn());
            if (existingBook != null && (newBook.getId() == null || !existingBook.getId().equals(newBook.getId()))) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate ISBN", "A book with this ISBN already exists."));
                return;
            }

            boolean isEdit = newBook.getId() != null;
            bookService.save(newBook);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book '" + newBook.getTitle() + "' " + (isEdit ? "was successfully updated." : "was successfully added.")));
            newBook = new Book(); // Reset input form
            refreshBooks(); // Refresh list
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save book: " + e.getMessage()));
        }
    }

    public void prepareEdit(Book book) {
        newBook = new Book();
        newBook.setId(book.getId());
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setIsbn(book.getIsbn());
    }

    public void cancelEdit() {
        newBook = new Book();
    }

    public void deleteBook(Long id) {
        try {
            bookService.delete(id);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book deleted successfully."));
            refreshBooks();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete book: " + e.getMessage()));
        }
    }

    public void search() {
        refreshBooks();
    }

    public void clearSearch() {
        searchQuery = null;
        refreshBooks();
    }

    // Getters and Setters
    public List<Book> getBooks() {
        return books;
    }

    public Book getNewBook() {
        return newBook;
    }

    public void setNewBook(Book newBook) {
        this.newBook = newBook;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
