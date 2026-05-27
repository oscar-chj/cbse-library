package com.upm.library;

import com.upm.library.persistence.Book;
import com.upm.library.persistence.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main entry point for the Library Catalog Management System.
 * Seeds sample book data on first run if the database is empty.
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    /**
     * Seeds the database with sample books if no records exist.
     * This ensures the application always has demo data for testing.
     */
    @Bean
    CommandLineRunner seedData(BookRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Book("Effective Java", "Joshua Bloch", "9780134685991"));
                repository.save(new Book("Clean Code", "Robert C. Martin", "9780132350884"));
                repository.save(new Book("Design Patterns", "Erich Gamma", "9780201633610"));
                System.out.println("Database seeded with 3 sample books.");
            }
        };
    }
}
