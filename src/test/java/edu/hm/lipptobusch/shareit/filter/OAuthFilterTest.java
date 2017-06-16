/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.filter;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.resource.MediaResource;
import edu.hm.lipptobusch.shareit.resource.ShareitServletContextListener;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class OAuthFilterTest {

    private AuthenticationFilter authFilter = new AuthenticationFilter(mockAuthenticationService());
    private AuthenticationService authenticationService = mockAuthenticationService();

    private final Response EXPECTED_TOKEN_NOT_VALID = Response.status(MediaServiceResult.TOKEN_NOT_VALID.getStatusCode()).entity(MediaServiceResult.TOKEN_NOT_VALID).build();
    private final Response EXPECTED_VALID_TOKEN = Response.status(MediaServiceResult.OK.getStatusCode()).entity(MediaServiceResult.OK).build();
    private final JSONObject USER_WITHOUT_ADMIN_RIGHTS = new JSONObject().put("username","Hannah").put("password", "Nana");



    /**
     * MockObject for calling AuthenticationFilter.
     *
     * @param uri Uri to the requested source
     * @param queryParameters needs the token with the key "token"
     * @return ContainerRequestContext-Object for calling filter-Method in AuthenticationFilter-Class
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
     * Mock-object for the OAuth-Server.
     *
     * @return AuthenticationService-Object for creating and validating tokens
     */
    private AuthenticationService mockAuthenticationService() {
        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        String validTestToken = "Hannah:1234567890";

        when(mockAuthenticationService.createToken(any())).then(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                if (invocation.getArgument(0).equals(USER_WITHOUT_ADMIN_RIGHTS)) {
                    return new JSONObject().put("token", validTestToken);
                } else {
                    return new JSONObject("{}");
                }
            }
        });

        when(mockAuthenticationService.checkToken(anyString())).then(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                if (invocation.getArgument(0).equals(validTestToken)) {
                    return new JSONObject().put("admin", "false");
                } else {
                    return new JSONObject("{}");
                }
            }
        });

        return mockAuthenticationService;
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
            authFilter.filter(requestContext);
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
        String token = authenticationService.createToken(USER_WITHOUT_ADMIN_RIGHTS).get("token").toString();

        MultivaluedMap<String,String> queryparams = new MultivaluedStringMap() {{
            add("token",token);
        }};
        Response actual = null;

        //act
        try {
            ContainerRequestContext requestContext = mockContainerRequest("shareit/media/books", queryparams);
            authFilter.filter(requestContext);
        } catch (URISyntaxException e) {
            //never happens
        } catch (WebApplicationException exForNotValidToken) {
            //happens only, if token is not valid
            actual = exForNotValidToken.getResponse();
        }

        //assert
        assertEquals(null, actual);
    }

}