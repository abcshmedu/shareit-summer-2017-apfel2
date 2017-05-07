package edu.hm.lipptobusch.shareit.resource;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Medium;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;


/**
 * Created by mx on 26.04.17.
 */
public class MediaResourceTest_First_Try {

    //----------------------------------------------
    //          Tests for addBook()
    //----------------------------------------------

    @Test
    public void testAddOneBookSuccessful(){
        //arrange
        MediaResource resource = new MediaResource();
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        Response expected = Response.status(200).entity(MediaServiceResult.OK).build();

        //act
        Response actual = resource.createBook(bookToAdd);
        Medium[] allBooks = (Medium[]) resource.getBooks().getEntity();

        //assert
        assertEquals(1, allBooks.length);
        assertEquals(bookToAdd,allBooks[0]);
        assertEquals(expected.getEntity(),actual.getEntity());
        assertEquals(expected.getStatus(),actual.getStatus());
    }

    @Test
    public void testAddOneBookWithDuplicateIsbn(){
        //arrange
        MediaResource resource = new MediaResource();
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        Response expected = Response.status(400).entity(MediaServiceResult.DUPLICATE_ISBN).build();

        //act
        resource.createBook(bookToAdd);
        Response actual = resource.createBook(bookToAdd);
        Medium[] allBooks = (Medium[]) resource.getBooks().getEntity();

        //assert
        assertEquals(1, allBooks.length);
        assertEquals(expected.getEntity(),actual.getEntity());
        assertEquals(expected.getStatus(),actual.getStatus());
    }

    @Test
    public void testAddOneBookWithInvalidIsbn(){
        //arrange
        MediaResource resource = new MediaResource();
        Book bookToAdd = new Book("title", "autor", "9783826801929");
        Response expected = Response.status(400).entity(MediaServiceResult.INVALID_ISBN).build();

        //act
        Response actual = resource.createBook(bookToAdd);
        Medium[] allBooks = (Medium[]) resource.getBooks().getEntity();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getEntity(),actual.getEntity());
        assertEquals(expected.getStatus(),actual.getStatus());
    }

    @Test
    public void testAddOneBookWithIncompleteArgumentsBecauseOfMissingAuthor(){
        //arrange
        MediaResource resource = new MediaResource();
        Book bookToAdd = new Book("title", "", "9783866801929");
        Response expected = Response.status(400).entity(MediaServiceResult.INCOMPLETE_ARGUMENTS).build();

        //act
        Response actual = resource.createBook(bookToAdd);
        Medium[] allBooks = (Medium[]) resource.getBooks().getEntity();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getEntity(),actual.getEntity());
        assertEquals(expected.getStatus(),actual.getStatus());
    }

    @Test
    public void testAddOneBookWithIncompleteArgumentsBecauseOfMissingTitle(){
        //arrange
        MediaResource resource = new MediaResource();
        Book bookToAdd = new Book("", "author", "9783866801929");
        Response expected = Response.status(400).entity(MediaServiceResult.INCOMPLETE_ARGUMENTS).build();

        //act
        Response actual = resource.createBook(bookToAdd);
        Medium[] allBooks = (Medium[]) resource.getBooks().getEntity();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getEntity(),actual.getEntity());
        assertEquals(expected.getStatus(),actual.getStatus());
    }
}
