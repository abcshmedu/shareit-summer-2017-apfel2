/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.businessLayer;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
    INVALID_BARCODE(400,"The Barcode is not valid"),
    INCOMPLETE_ARGUMENTS(400,"Author or title is missing"),
    ISBN_NOT_FOUND(400,"Could not update book. ISBN does not exist."),
    BARCODE_NOT_FOUND(400,"Could not update disc. Barcode does not exist."),
    MODIFYING_ISBN_NOT_ALLOWED(400,"Modifying the isbn is not allowed. Difference between JSON and isbn-param."),
    MODIFYING_BARCODE_NOT_ALLOWED(400,"Modifying the barcode is not allowed. Difference between JSON and barcode-param."),
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
