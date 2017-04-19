/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.businessLayer;

import edu.hm.models.Book;
import edu.hm.models.Disc;
import edu.hm.models.Medium;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public interface MediaService {

    MediaServiceResult addBook(Book book);

    MediaServiceResult addDisc(Disc disc);

    Medium[] getBooks();

    Medium[] getDiscs();

    MediaServiceResult updateBook(Book book);

    MediaServiceResult updateDisc(Disc disc);
}
