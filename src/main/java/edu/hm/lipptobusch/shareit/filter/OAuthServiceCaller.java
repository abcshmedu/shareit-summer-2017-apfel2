/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.filter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-06-11
 */
public class OAuthServiceCaller implements AuthenticationService{

    private final String urlLocal = "http://localhost:8333/shareit/users/login/";
    private final String urlHeroku = "https://jularo.herokuapp.com/shareit/users/login/";

    /**
     * Call OAuthServer for validating a token.
     *
     * @param token token as string
     * @return JSON with information, if user is admin or not. Empty string if token is not valid.
     */
    @Override
    public JSONObject checkToken(String token) {

        String result = "";

        try {
            URL url = new URL(urlLocal + token);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("GET"); // PUT is another valid option
            http.setDoOutput(true);

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

        if (result.isEmpty()) {
            result = "{}";
        }

        return new JSONObject(result);
    }



    /**
     * Call OAuth-Server and generate token.
     *
     * @param user JSONObject with already registered user at the OAuth-Server
     * @return valid token for this user and his scope
     */
    @Override
    public JSONObject createToken(JSONObject user) {
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

        return new JSONObject(result);
    }
}
