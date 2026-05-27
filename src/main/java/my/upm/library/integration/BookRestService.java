package my.upm.library.integration;

import my.upm.library.business.BookServiceBean;
import my.upm.library.persistence.Book;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookRestService {

    @EJB
    private BookServiceBean bookService;

    @GET
    @Path("/{isbn}")
    public Response getBookByIsbn(@PathParam("isbn") String isbn) {
        Book book = bookService.findByIsbn(isbn);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }
}
