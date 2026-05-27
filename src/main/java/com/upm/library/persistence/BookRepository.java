package com.upm.library.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for the Book entity.
 * Provides CRUD operations and custom query methods.
 *
 * This is the Data Access Component in the CBSE layered architecture,
 * abstracting direct SQL statements behind JPA query derivation.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds books whose title contains the given string (case-insensitive).
     * Used by UC-3: Search Book by Title.
     *
     * @param title the search query
     * @return list of matching books
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds a book by its exact ISBN.
     * Used by UC-5: Fetch Book Details via Web Service.
     *
     * @param isbn the ISBN to look up
     * @return an Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);
}
