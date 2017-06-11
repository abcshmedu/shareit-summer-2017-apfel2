/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.filter;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.resource.ShareitServletContextListener;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Every request gets filtered by this class.
 * AuthenticationFilter asks a Authentication-Service if the token is valid.
 * The filter method reads the token from the query param "token".
 *
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-06-11
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws WebApplicationException {

        MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();

        String token = queryParams.getFirst("token");

        AuthenticationService authenticationService = ShareitServletContextListener.getInjectorInstance().getInstance(AuthenticationService.class);

        if (authenticationService.checkToken(token).toString().equals("{}")) {
            //deliver custom response with error-code and message
            throw new WebApplicationException(Response.status(MediaServiceResult.TOKEN_NOT_VALID.getStatusCode()).entity(MediaServiceResult.TOKEN_NOT_VALID).build());
        }
    }
}
