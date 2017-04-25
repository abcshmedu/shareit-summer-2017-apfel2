/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.businessLayer;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public enum MediaServiceResult {

    /*
        Wenn kein Fehler auftritt, dann reicht es aus, wenn die aufgerufene Methode
        einer Resourcen-Klasse den entsprechenden 2xx-Code zuruck gibt. Tritt ein Fehler auf,
        so muss ein angemessener HTTP-Fehlercode an den Client geliefert werden und sinnvollerweise
        ein Hinweis auf die Fehlerursache.
        Liefern Sie in diesem Fall ein JSON-Objekt mit denn Attributen code (Zahl) und detail
        (Zeichenkette). Der Fehlercode wird dann also redundant uebertragen.
     */


    /*
        HTTP-Status-Codes

        200     OK
        201     Created
        204     No Content (z.B. nach erfolgreichem DELETE)

        400	    Bad Request
        401     Unauthorized
        404     Not found

        500     Internal Server Error
        501     Not Implemented
        503     Service unavailable

     */

    DUPLICATE_ISBN(400,"The ISBN already exists"),
    DUPLICATE_Barcode(400,"The Barcode already exists"),
    INVALID_ISBN(400,"The ISBN is not valid"),
    INCOMPLETE_ARGUMENTS(400,"Author or title is missing"),
    OK(200,"OK 123");


    private final int statusCode;
    private final String message;

    MediaServiceResult(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }

    private MediaServiceResult() {statusCode = 0; message = "";}

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
