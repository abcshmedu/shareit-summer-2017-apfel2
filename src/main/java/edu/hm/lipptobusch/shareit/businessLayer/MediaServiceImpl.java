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
    public Medium getBook(String isbn) {
        Medium result = books.get(isbn);

        return result;
    }

    @Override
    public Medium getDisc(String barcode) {
        Medium result = discs.get(barcode);

        return result;
    }

    @Override
    public MediaServiceResult updateBook(Book book, String isbn) {
        if (!book.getIsbn().equals(isbn)) {
            //modifying is ISBN is not allowed
            return MediaServiceResult.MODIFYING_ISBN_NOT_ALLOWED;
        }

        if (!books.containsKey(book.getIsbn())) {
            //ISBN not found
            return MediaServiceResult.ISBN_NOT_FOUND;
        }

        if (book.getAuthor().isEmpty() || book.getTitle().isEmpty()) {
            //author and title are missing
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        MediaServiceResult result;

        Book oldBook = books.get(book.getIsbn());
        books.remove(oldBook);

        String newTitle = book.getTitle().isEmpty()?oldBook.getTitle():book.getTitle();
        String newAuthor = book.getAuthor().isEmpty()?oldBook.getAuthor():book.getAuthor();

        Book newBook = new Book(newTitle,newAuthor,oldBook.getIsbn());

        books.put(newBook.getIsbn(), newBook);


        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult updateDisc(Disc disc, String barcode) {
        if (!disc.getBarcode().equals(disc)) {
            //modifying is Barcode is not allowed
            return MediaServiceResult.MODIFYING_BARCODE_NOT_ALLOWED;
        }

        if (!books.containsKey(disc.getBarcode())) {
            //Barcode not found
            return MediaServiceResult.BARCODE_NOT_FOUND;
        }

        if (disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk() == -1) {
            //author, title and fsk are missing
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        MediaServiceResult result;

        Disc oldDisc = discs.get(disc.getBarcode());
        discs.remove(oldDisc);

        String newTitle = disc.getTitle().isEmpty()?oldDisc.getTitle():disc.getTitle();
        String newDirector = disc.getDirector().isEmpty()?oldDisc.getDirector():disc.getDirector();
        int newfsk = disc.getFsk() == -1?oldDisc.getFsk():disc.getFsk();

        Disc newDisc = new Disc(newTitle,oldDisc.getBarcode(),newDirector,newfsk);

        discs.put(newDisc.getBarcode(), newDisc);


        return MediaServiceResult.OK;
    }


    private boolean isbnIsValid(String isbn) {
        //TODO algorithm for checking isbn
        //https://en.wikipedia.org/wiki/International_Standard_Book_Number#Check_digits
        return true;
    }

    private boolean barcodeIsValid(String barcode) {
        //TODO algorithm for checking barcode
        return true;
    }
}
