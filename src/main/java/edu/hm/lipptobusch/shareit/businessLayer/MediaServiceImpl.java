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
import java.util.stream.Collectors;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class MediaServiceImpl implements MediaService{
    private final Map<String, Book> books;
    private final Map<String, Disc> discs;

    public MediaServiceImpl() {
        this.books = new HashMap<>();
        this.discs = new HashMap<>();
    }

    @Override
    public MediaServiceResult addBook(Book book) {
        //TODO dealing with errors (author or title is missing; duplicate ISBN; invalid ISBN)

        if (books.containsKey(book.getIsbn())) {
            //Error duplicate ISBN
            //return MediaServiceResult
            return MediaServiceResult.DUPLICATE_ISBN;
        }

        if (!isbnIsValid(book.getIsbn())) {
            return MediaServiceResult.INVALID_ISBN;
        }

        if (book.getAuthor().isEmpty() || book.getTitle().isEmpty()) {
            //Error no author
            //return MediaServiceResult
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        books.put(book.getIsbn(), book);

        System.out.println("added book: " + books.containsKey(book.getIsbn())); //TODO DELETE testing line

        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {
        //TODO dealing with errors (director or title or fsk is missing; duplicate barcode; invalid barcode)
        //error values for Strings are null and for fsk -1

        if (discs.containsKey(disc.getBarcode())) {
            //Error duplicate ISBN
            //return MediaServiceResult
            return MediaServiceResult.DUPLICATE_Barcode;
        }

        if (!barcodeIsValid(disc.getBarcode())) {
            return MediaServiceResult.INVALID_BARCODE;
        }

        if (disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk()==-1) {
            //Error no author
            //return MediaServiceResult
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        discs.put(disc.getBarcode(), disc);

        System.out.println("added disc: " + discs.containsKey(disc.getBarcode())); //TODO DELETE testing line


        return MediaServiceResult.OK;
    }

    @Override
    public Medium[] getBooks() {
        Medium[] result = new Medium[books.size()];

        Iterator<Book> mediumIterator = books.values().iterator();

        for (int i = 0; mediumIterator.hasNext(); i++) {
            result[i] = mediumIterator.next();
        }

        return result;
    }

    @Override
    public Medium[] getDiscs() {
        Medium[] result = new Medium[discs.size()];
        Iterator<Disc> mediumIterator = discs.values().iterator();

        for (int i = 0; mediumIterator.hasNext(); i++) {
            result[i] = mediumIterator.next();
        }

        return result;
    }

    @Override
    public Medium getBook() {
        return null;
    }

    @Override
    public Medium getDisc() {
        return null;
    }

    @Override
    public MediaServiceResult updateBook(Book book, String isbn) { //TODO maybe modify interface, need isbn from uri for correct check
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
    public MediaServiceResult updateDisc(Disc disc, String barcode) {
        return null;
    }


    private boolean isbnIsValid(String isbnCode) {
        final int[] isbn = isbnCode.chars()
                .map(x -> Character.getNumericValue(x))
                .toArray();

        int sum = 0;
        if(isbn.length == 10) {
            for(int i = 0; i < 10; i++) {
                sum += i * isbn[i]; //asuming this is 0..9, not '0'..'9'
            }

            if(isbn[9] == sum % 11) return true;
        } else if(isbn.length == 13) {

            for(int i = 0; i < 12; i++) {
                if(i % 2 == 0) {
                    sum += isbn[i]; //asuming this is 0..9, not '0'..'9'
                } else {
                    sum += isbn[i] * 3;
                }
            }

            if(isbn[12] == 10 - (sum % 10)) return true;
        }

        return false;
    }

    private boolean barcodeIsValid(String barcode) {
        //TODO algorithm for checking barcode
        return true;
    }
}
