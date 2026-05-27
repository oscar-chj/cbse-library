package com.upm.library.business;

import com.upm.library.persistence.Book;
import com.upm.library.persistence.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business Service Component for the Library Catalog.
 * Manages transaction boundaries and applies core data validation logic.
 *
 * This is the Business Service Component in the CBSE layered architecture.
 */
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructor injection of the BookRepository.
     *
     * @param bookRepository the data access component
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves all books in the catalog.
     * Used by UC-2: View All Books.
     *
     * @return list of all books
     */
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Searches for books whose title contains the given query string.
     * Used by UC-3: Search Book by Title.
     *
     * @param query the search term
     * @return list of matching books
     */
    @Transactional(readOnly = true)
    public List<Book> findByTitle(String query) {
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCase(query.trim());
    }

    /**
     * Finds a book by its exact ISBN.
     * Used by UC-5: Fetch Book Details via Web Service.
     *
     * @param isbn the ISBN to look up
     * @return an Optional containing the book if found
     */
    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * Saves a new book to the catalog after validation.
     * Used by UC-1: Add New Book.
     *
     * @param book the book to save
     * @return the saved book with its generated ID
     * @throws IllegalArgumentException if validation fails
     */
    public Book save(Book book) {
        // Validate required fields
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required.");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required.");
        }

        // Check for duplicate ISBN
        Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn().trim());
        if (existing.isPresent() && !existing.get().getId().equals(book.getId())) {
            throw new IllegalArgumentException("A book with ISBN '" + book.getIsbn() + "' already exists.");
        }

        // Trim whitespace
        book.setTitle(book.getTitle().trim());
        book.setAuthor(book.getAuthor().trim());
        book.setIsbn(book.getIsbn().trim());

        return bookRepository.save(book);
    }

    /**
     * Deletes a book by its ID.
     * Used by UC-4: Delete Book Record.
     *
     * @param id the ID of the book to delete
     */
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
