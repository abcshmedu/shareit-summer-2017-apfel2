/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.resource;


import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import edu.hm.lipptobusch.shareit.businessLayer.MediaService;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceImpl;
import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
@Path("media")
public class MediaResource {

    //Dependency Injection without annotations
    private static MediaService mediaService = ShareitServletContextListener.getInjectorInstance().getInstance(MediaService.class);


    //Better way of Dependency Injection with Annotations, but not working yet
    /*private MediaService mediaService;

    @Inject
    public MediaResource(MediaService mediaService) {
        this.mediaService = mediaService;
    }*/

    /**
     * URI-Template     Verb    Wirkung
     * /media/books     POST    Neues Medium Buch anlegen
     *
     * .../media/books?token=asdkfjpaweoi
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

        return Response.status(result.getStatusCode()).entity(result).build();
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

        return Response.status(Response.Status.OK).entity(allBooks).build();
    }

    @GET
    @Path("books/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {

        Medium book = mediaService.getBook(isbn);

        return Response.status(200).entity(book).build();
    }


    @POST
    @Path("discs")
    @Consumes(MediaType.APPLICATION_JSON) //Jersey will use Jackson to handle the JSON conversion automatically
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDisc(Disc disc) {

        MediaServiceResult result = mediaService.addDisc(disc);

        return Response.status(result.getStatusCode()).entity(result).build();
    }


    @GET
    @Path("discs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscs() {

        Medium[] allBooks = mediaService.getDiscs();

        return Response.status(200).entity(allBooks).build();
    }

    @GET
    @Path("discs/{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDisc(@PathParam("barcode") String barcode) {

        Medium disc = mediaService.getDisc(barcode);

        return Response.status(200).entity(disc).build();
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
    public Response updateBook(@PathParam("isbn") String isbn, Book book) {

        MediaServiceResult result = mediaService.updateBook(book, isbn);

        return Response.status(result.getStatusCode()).entity(result).build();
    }


    @PUT
    @Path("discs/{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDisc(@PathParam("barcode") String barcode, Disc disc) {

        MediaServiceResult result = mediaService.updateDisc(disc, barcode);

        return Response.status(result.getStatusCode()).entity(result).build();
    }

    /**
     * Resetting the database for testing.
     */
    public void resetDataBase() {
        mediaService.clearMap();
    }

}
