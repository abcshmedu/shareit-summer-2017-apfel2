/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.filter;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.filter.OAuthFilter;
import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.resource.MediaResource;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class OAuthFilterTest {

    private MediaResource mediaResource = new MediaResource();
    private OAuthFilter oAuthFilter = new OAuthFilter();

    private final Response EXPECTED_TOKEN_NOT_VALID = Response.status(MediaServiceResult.TOKEN_NOT_VALID.getStatusCode()).entity(MediaServiceResult.TOKEN_NOT_VALID).build();
    private final Response EXPECTED_VALID_TOKEN = Response.status(MediaServiceResult.OK.getStatusCode()).entity(MediaServiceResult.OK).build();
    private final JSONObject USER_WITHOUT_ADMIN_RIGHTS = new JSONObject().put("username","Hannah").put("password", "Nana");


    /**
     * Reset the database if books and discs before every test.
     */
    @Before
    public void resetDataBase(){
        mediaResource.resetDataBase();
    }

    /**
     * MockObject for calling OAuthFilter.
     *
     * @param uri Uri to the requested source
     * @param queryParameters needs the token with the key "token"
     * @return ContainerRequestContext-Objekt for calling filter-Method in OAuthFilter-Class
     * @throws URISyntaxException exception if the uri cannot be build
     */
    private ContainerRequestContext mockContainerRequest(String uri, MultivaluedMap<String, String> queryParameters) throws URISyntaxException {
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        UriInfo uriInfo = mock(UriInfo.class);
        URI requestUri = new URI(uri);
        when(uriInfo.getRequestUri()).thenReturn(requestUri);
        when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
        when(requestContext.getUriInfo()).thenReturn(uriInfo);

        return requestContext;
    }

    /**
     * Call OAuth-Server and generate token.
     *
     * @param user JSONObject with already registered user at the OAuth-Server
     * @return valid token for this user and his scope
     */
    private String generateToken(JSONObject user) {
        String urlLocal = "http://localhost:8333/shareit/users/login/";
        String urlHeroku = "https://jularo.herokuapp.com/shareit/users/login/";

        String result = "";


        try {
            URL url = new URL(urlLocal);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.setDoOutput(true);
            http.setDoInput(true);

            OutputStream os = http.getOutputStream();
            os.write(user.toString().getBytes("UTF-8"));
            os.close();


            if (200 == http.getResponseCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
                String currentLine = "";
                while ((currentLine = br.readLine()) != null) {
                    result += currentLine;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Test
    public void testAddBookWithNotValidToken() {
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        MultivaluedMap<String,String> queryparams = new MultivaluedStringMap() {{
            add("token","21435465");
        }};
        Response actual = null;

        //act
        try {
            ContainerRequestContext requestContext = mockContainerRequest("shareit/media/books", queryparams);
            oAuthFilter.filter(requestContext);
        } catch (URISyntaxException e) {
            //never happens
        } catch (WebApplicationException exForNotValidToken) {
            //should happen
            actual = exForNotValidToken.getResponse();
        }

        //assert
        assertEquals(EXPECTED_TOKEN_NOT_VALID.getEntity(), actual.getEntity());
        assertEquals(EXPECTED_TOKEN_NOT_VALID.getStatus(), actual.getStatus());

    }


    @Test
    public void testAddBookWithValidToken() {
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        String token = new JSONObject(generateToken(USER_WITHOUT_ADMIN_RIGHTS)).get("token").toString();
        MultivaluedMap<String,String> queryparams = new MultivaluedStringMap() {{
            add("token",token);
        }};
        Response actual = null;

        //act
        try {
            ContainerRequestContext requestContext = mockContainerRequest("shareit/media/books", queryparams);
            oAuthFilter.filter(requestContext);
        } catch (URISyntaxException e) {
            //never happens
        } catch (WebApplicationException exForNotValidToken) {
            //should happen
            actual = exForNotValidToken.getResponse();
        }

        //assert
        assertEquals(null, actual);
    }

}