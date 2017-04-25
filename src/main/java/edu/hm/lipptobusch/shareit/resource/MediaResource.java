/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.resource;


import edu.hm.lipptobusch.shareit.businessLayer.MediaService;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceImpl;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
@Path("media")
public class MediaResource{

    private static final MediaService mediaService = new MediaServiceImpl();

    /**
     * URI-Template     Verb    Wirkung
     * /media/books     POST    Neues Medium Buch anlegen
     *
     * Moeglicher Fehler: Ungueltige ISBN
     * Moeglicher Fehler: ISBN bereits vorhanden
     * Moeglicher Fehler: Autor oder Titel fehlt
     *
     * @param book
     * @return
     */
    @POST
    @Path("books")
    @Consumes(MediaType.APPLICATION_JSON) //Jersey will use Jackson to handle the JSON conversion automatically
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {

        MediaServiceResult result = mediaService.addBook(book);

        /**
        String json = null;
         ObjectMapper mapper = new ObjectMapper();
        try {
            json= mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json); **/

        return Response.status(result.getStatusCode()).entity(result).build(); //TODO correct response via MediaServiceResult
    }

    /**
     * URI-Template     Verb    Wirkung
     * /media/books     GET     Alle Buecher auflisten
     * @return
     */
    @GET
    @Path("books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {

        Medium[] allBooks = mediaService.getBooks();

        return Response.status(201).entity(allBooks).build(); //TODO correct response via MediaServiceResult
    }


    @POST
    @Path("discs")
    @Consumes(MediaType.APPLICATION_JSON) //Jersey will use Jackson to handle the JSON conversion automatically
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDisc(Disc disc) {

        MediaServiceResult result = mediaService.addDisc(disc);

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json= mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(result.getMessage());
        System.out.println(json);

        return Response.status(result.getStatusCode()).entity(result).build(); //TODO correct response via MediaServiceResult
    }


    @GET
    @Path("discs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscs() {

        Medium[] allBooks = mediaService.getDiscs();

        return Response.status(201).entity(allBooks).build(); //TODO correct response via MediaServiceResult
    }

    /**
     * URI-Template         Verb    Wirkung
     * /media/books/{isbn}  PUT     Daten zu vorhandenem Buch modifizieren
     *
     * JSONDaten enthalten nur die zu modifizierenden Attribute
     * Moeoglicher Fehler: ISBN nicht gefunden
     * Moeglicher Fehler: ISBN soll modifiziert werden (also die JSON-Daten enthalten eine andere ISBN als die URI)
     * Moeglicher Fehler: Autor und Titel fehlen
     *
     * @param isbn
     * @return
     */
    @PUT
    @Path("books/{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn) {

        return Response.status(201).build(); //TODO correct response via MediaServiceResult
    }

}
