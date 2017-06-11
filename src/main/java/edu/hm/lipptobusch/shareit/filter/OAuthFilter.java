package edu.hm.lipptobusch.shareit.filter;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Every request gets filtered by this class.
 * OAuthFilter asks the OAuth-Server if the token is valid.
 * The filter method reads the token from the query param "token".
 *
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-06-11
 */
@Provider
public class OAuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws WebApplicationException {

        MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();

        String token = queryParams.getFirst("token");

        if (callOAuthServer(token).isEmpty()) {
            //deliver custom response with error-code and message
            throw new WebApplicationException(Response.status(MediaServiceResult.TOKEN_NOT_VALID.getStatusCode()).entity(MediaServiceResult.TOKEN_NOT_VALID).build());
        }
    }


    /**
     * Call OAuthServer for validating a token.
     *
     * @param token token as string
     * @return JSON with information, if user is admin or not. Empty string if token is not valid.
     */
    private String callOAuthServer(String token) {
        //call oAuth-Server
        // get "admin": "true"
        // or empty String if token is not valid

        String urlLocal = "http://localhost:8333/shareit/users/login/";
        String urlHeroku = "https://jularo.herokuapp.com/shareit/users/login/";


        String result = "";

        //System.out.println("token: " + token);

        try {
            URL url = new URL(urlLocal + token);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("GET"); // PUT is another valid option
            http.setDoOutput(true);

            //System.out.println(http.getResponseMessage());    //OK
            //System.out.println(http.getResponseCode());       //200

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
}
