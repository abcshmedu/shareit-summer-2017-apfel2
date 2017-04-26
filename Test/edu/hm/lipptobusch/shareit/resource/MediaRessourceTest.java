package edu.hm.lipptobusch.shareit.resource;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

/**
 * Created by mx on 26.04.17.
 */
public class MediaRessourceTest {
    @Test
    public void createBookTest(){
        Response response = new MediaResource().createBook(new Book("title", "autor", "9783866801929"));

        assertEquals(Response.status(200).entity(MediaServiceResult.OK).build(),response);

    }
}
