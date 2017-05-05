package edu.hm.lipptobusch.shareit.resource;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;


/**
 * Created by mx on 26.04.17.
 */
public class MediaResourceTest_First_Try {

    @Test
    public void createBookTest(){
        Response expected = Response.status(200).entity(MediaServiceResult.OK).build();
        Response expected2 = Response.status(200).entity(MediaServiceResult.OK).build();

        Response response = new MediaResource().createBook(new Book("title", "autor", "9783866801929"));
        System.out.println(response.hashCode());
        System.out.println(expected.hashCode());
        System.out.println(expected2.hashCode());
        assertEquals(expected,expected2);

    }

}
