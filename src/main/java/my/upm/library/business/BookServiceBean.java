package my.upm.library.business;

import my.upm.library.persistence.Book;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@DataSourceDefinition(
    name = "java:global/jdbc/LibraryDB",
    className = "com.mysql.cj.jdbc.MysqlDataSource",
    url = "jdbc:mysql://db:3306/library_db?allowPublicKeyRetrieval=true&useSSL=false",
    user = "libraryuser",
    password = "librarypass"
)
@Stateless
public class BookServiceBean {

    @PersistenceContext(unitName = "LibraryPU")
    private EntityManager em;

    public void save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
        } else {
            em.merge(book);
        }
    }

    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:title)", Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    public Book findByIsbn(String isbn) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        List<Book> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public void delete(Long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
