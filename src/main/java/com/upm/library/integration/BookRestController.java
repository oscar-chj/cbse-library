package com.upm.library.integration;

import com.upm.library.business.BookService;
import com.upm.library.persistence.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Integration Endpoint for the Library Catalog.
 * Exposes public endpoints for external integration queries,
 * bypassing the Vaadin state machine completely.
 *
 * This is the Integration Component in the CBSE layered architecture.
 */
@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    /**
     * Constructor injection of the BookService.
     *
     * @param bookService the business service component
     */
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Fetches a book by its ISBN.
     * Used by UC-5: Fetch Book Details via Web Service.
     *
     * Example: GET /api/books/9780134685991
     *
     * @param isbn the ISBN to look up
     * @return 200 OK with the book JSON, or 404 Not Found
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
