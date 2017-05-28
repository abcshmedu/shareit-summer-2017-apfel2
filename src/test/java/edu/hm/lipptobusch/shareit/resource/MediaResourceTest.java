/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.resource;

import edu.hm.lipptobusch.shareit.businessLayer.MediaServiceResult;
import edu.hm.lipptobusch.shareit.models.Book;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class MediaResourceTest {

    private MediaResource mediaResource = new MediaResource();
    private final Response EXPECTED_TOKEN_NOT_VALID = Response.status(MediaServiceResult.TOKEN_NOT_VALID.getStatusCode()).entity(MediaServiceResult.TOKEN_NOT_VALID).build();
    private final Response EXPECTED_VALID_TOKEN = Response.status(MediaServiceResult.OK.getStatusCode()).entity(MediaServiceResult.OK).build();

    private final JSONObject USER_WITHOUT_ADMIN_RIGHTS = new JSONObject().put("username","Hannah").put("password", "Nana");

    @Before
    public void resetDataBase(){
        mediaResource.resetDataBase();
    }

    private String generateToken(JSONObject user) {
        String urlLocal = "http://localhost:8333/shareit/users/login/";
        String urlHeroku = "https://jularo.herokuapp.com/shareit/users/login/";

        String result = "";


        try {
            URL url = new URL(urlHeroku);
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
        String token = "21435465";

        //act
        Response actual = mediaResource.createBook(bookToAdd,token);

        //assert
        assertEquals(EXPECTED_TOKEN_NOT_VALID.getEntity(), actual.getEntity());
        assertEquals(EXPECTED_TOKEN_NOT_VALID.getStatus(), actual.getStatus());
    }

    @Test
    public void testAddBookWithValidToken() {
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        String token = generateToken(USER_WITHOUT_ADMIN_RIGHTS);

        //act
        Response actual = mediaResource.createBook(bookToAdd,token);

        //assert
        assertEquals(EXPECTED_VALID_TOKEN.getEntity(), actual.getEntity());
        assertEquals(EXPECTED_VALID_TOKEN.getStatus(), actual.getStatus());
    }

}