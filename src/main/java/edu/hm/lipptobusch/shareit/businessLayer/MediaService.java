/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.businessLayer;

import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;

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

    Medium getBook(String isbn);

    Medium getDisc(String barcode);

    MediaServiceResult updateBook(Book book, String isbn);

    MediaServiceResult updateDisc(Disc disc, String barcode);
}
