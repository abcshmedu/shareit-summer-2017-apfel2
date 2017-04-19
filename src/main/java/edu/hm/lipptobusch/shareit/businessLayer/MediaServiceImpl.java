/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.businessLayer;

import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;

import java.util.*;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class MediaServiceImpl implements MediaService{
    private final Map<String, Book> books;
    private final Collection<Disc> discs;

    public MediaServiceImpl() {
        this.books = new HashMap<>();
        this.discs = new HashSet<>();
    }

    @Override
    public MediaServiceResult addBook(Book book) {
        //TODO dealing with errors (author or title is missing; duplicate ISBN; invalid ISBN)
        //error values for Strings are null

        books.put(book.getIsbn(), book);

        return null;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {
        //TODO dealing with errors (director or title or fsk is missing; duplicate barcode; invalid barcode)
        //error values for Strings are null and for fsk -1

        discs.add(disc);

        return null;
    }

    @Override
    public Medium[] getBooks() {
        Medium[] result = new Medium[books.size()];

        Iterator<Book> mediumIterator = books.values().iterator();

        for (int i = 0; i < result.length; i++) {
            result[i] = mediumIterator.next();
        }

        return result;
    }

    @Override
    public Medium[] getDiscs() {
        Medium[] result = new Medium[discs.size()];
        Iterator<Disc> mediumIterator = discs.iterator();

        for (int i = 0; i < result.length; i++) {
            result[i] = mediumIterator.next();
        }

        return result;
    }

    @Override
    public MediaServiceResult updateBook(Book book) { //TODO maybe modify interface, need isbn from uri for correct check
        /*
            Daten zu vorhandenem Buch modizieren (JSONDaten
            enthalten nur die zu modizierenden Attribute)
            Moglicher Fehler: ISBN nicht gefunden
            Moglicher Fehler: ISBN soll modiziert werden (also
            die JSON-Daten enthalten eine andere ISBN als die
            URI)
            Moglicher Fehler: Autor und Titel fehlen
         */


        MediaServiceResult result;


        if (books.containsKey(book.getIsbn())) {
            Book oldBook = books.get(book.getIsbn());

            String titleUpdateBook = book.getTitle();
            String authorUpdateBook = book.getAuthor();
            String isbnUpdateBook = book.getIsbn();

            if (isbnUpdateBook == null) {

            } else {

            }


        } else {
            //TODO error: ISBN not found
        }

        return null; //TODO return result
    }

    @Override
    public MediaServiceResult updateDisc(Disc disc) {
        return null;
    }

    public static void main(String[] args) {
        MediaServiceImpl ms = new MediaServiceImpl();
        ms.addBook(new Book("Bible","god","123456"));
        ms.addBook(new Book("Bible2","god2.0","1234546"));
        Medium[] allBooks = ms.getBooks();
        for (Medium m: allBooks) {
            System.out.println(m.toString());
        }
    }
}
