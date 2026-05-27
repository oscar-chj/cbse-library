package my.upm.library.business;

import my.upm.library.persistence.Book;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class DatabaseSeeder {

    @EJB
    private BookServiceBean bookService;

    @PostConstruct
    public void init() {
        try {
            if (bookService.findAll().isEmpty()) {
                bookService.save(new Book("Effective Java", "Joshua Bloch", "9780134685991"));
                bookService.save(new Book("Clean Code", "Robert C. Martin", "9780132350884"));
                bookService.save(new Book("Design Patterns", "Erich Gamma", "9780201633610"));
                System.out.println("[DatabaseSeeder] Successfully seeded database with 3 catalog books.");
            } else {
                System.out.println("[DatabaseSeeder] Database already contains records. Skipping seeding.");
            }
        } catch (Exception e) {
            System.err.println("[DatabaseSeeder] Error occurred while seeding database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
