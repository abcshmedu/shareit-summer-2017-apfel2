/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */
package edu.hm.lipptobusch.shareit.filter;

import org.json.JSONObject;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-06-11
 */
public interface AuthenticationService {
    /**
     * Checks whether the given token is valid or not.
     *
     * @param token A unique token
     * @return Admin status of the user when valid otherwise an empty string
     */
    JSONObject checkToken(String token);

    /**
     * Creates a new unique token.
     *
     * @param user The user the token is created for
     * @return The created token
     */
    JSONObject createToken(JSONObject user);
}
