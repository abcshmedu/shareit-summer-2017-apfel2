/**
 * Organisation: Hochschule Muenchen, Fakultaet 07 Informatik und Mathematik
 * Purpose: lab software-architecture, IF4B, SS2017
 * Purpose: solution of assignment 2
 */

package edu.hm.lipptobusch.shareit.businessLayer;

import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;
import edu.hm.lipptobusch.shareit.persistence.HibernatePersistence;
import edu.hm.lipptobusch.shareit.persistence.HibernatePersistenceImpl;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class MediaServiceImpl implements MediaService{
    //private final Map<String, Book> books;
    //private final Map<String, Disc> discs;
    private final HibernatePersistence hibernatePersistence;

    @Inject
    public MediaServiceImpl(HibernatePersistence hibernatePersistence) {
        //this.books = new HashMap<>();
        //this.discs = new HashMap<>();
        this.hibernatePersistence = hibernatePersistence;
    }

    @Override
    public MediaServiceResult addBook(Book book) {
        book = new Book(book.getTitle(),book.getAuthor(),deleteDashesInIsbn(book.getIsbn()));

        if (getBook(book.getIsbn()) != null) {
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

        //books.put(book.getIsbn(), book);
        hibernatePersistence.addMedium(book);
        //hibernatePersistence.updateMedium(book);
        //hibernatePersistence.updateMedium(new Book("myTitle","bla","9783866801929"));
        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult addDisc(Disc disc) {

        if (getDisc(disc.getBarcode()) != null) {
            //Error duplicate ISBN
            //return MediaServiceResult
            return MediaServiceResult.DUPLICATE_Barcode;
        }

        if (!barcodeIsValid(disc.getBarcode()) || disc.getBarcode().isEmpty()) {
            return MediaServiceResult.INVALID_BARCODE;
        }

        if (disc.getDirector().isEmpty() || disc.getTitle().isEmpty() || disc.getFsk()==-1) {
            //Error no author
            //return MediaServiceResult
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        //discs.put(disc.getBarcode(), disc);
        hibernatePersistence.addMedium(disc);

        return MediaServiceResult.OK;
    }

    @Override
    public Medium[] getBooks() {

        List<Medium> table = hibernatePersistence.getTable(Book.class);

        return table.toArray(new Medium[table.size()]);
    }

    @Override
    public Medium[] getDiscs() {
        List<Medium> table = hibernatePersistence.getTable(Disc.class);

        return table.toArray(new Medium[table.size()]);
    }

    @Override
    public Medium getBook(String isbn) {

        Optional<Medium> result = hibernatePersistence.getTable(Book.class).stream().filter(medium -> ((Book)medium).getIsbn().equals(deleteDashesInIsbn(isbn))).findFirst();

        return result.orElse(null);
    }

    @Override
    public Medium getDisc(String barcode) {

        Optional<Medium> result = hibernatePersistence.getTable(Disc.class).stream().filter(medium -> ((Disc)medium).getBarcode().equals(barcode)).findFirst();

        return result.orElse(null);
    }

    @Override
    public MediaServiceResult updateBook(Book book, String isbn) {
        book = new Book(book.getTitle(),book.getAuthor(),deleteDashesInIsbn(book.getIsbn()));
        isbn = deleteDashesInIsbn(isbn);

        if (!book.getIsbn().equals(isbn)) {
            //modifying is ISBN is not allowed
            return MediaServiceResult.MODIFYING_ISBN_NOT_ALLOWED;
        }

        if (getBook(book.getIsbn()) == null) {
            //ISBN not found
            return MediaServiceResult.ISBN_NOT_FOUND;
        }

        if (book.getAuthor().isEmpty() && book.getTitle().isEmpty()) {
            //author and title are missing
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        MediaServiceResult result;

        Book oldBook = (Book) getBook(book.getIsbn());
        //books.remove(oldBook);

        String newTitle = book.getTitle().isEmpty()?oldBook.getTitle():book.getTitle();
        String newAuthor = book.getAuthor().isEmpty()?oldBook.getAuthor():book.getAuthor();

        Book newBook = new Book(newTitle,newAuthor,oldBook.getIsbn());

        //books.put(newBook.getIsbn(), newBook);
        hibernatePersistence.updateMedium(newBook);

        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult updateDisc(Disc disc, String barcode) {
        if (!disc.getBarcode().equals(barcode)) {
            //modifying is Barcode is not allowed
            return MediaServiceResult.MODIFYING_BARCODE_NOT_ALLOWED;
        }

        if (getDisc(disc.getBarcode()) == null) {
            //Barcode not found
            return MediaServiceResult.BARCODE_NOT_FOUND;
        }

        if (disc.getDirector().isEmpty() && disc.getTitle().isEmpty() && disc.getFsk() == -1) {
            //author, title and fsk are missing
            return MediaServiceResult.INCOMPLETE_ARGUMENTS;
        }

        MediaServiceResult result;

        Disc oldDisc = (Disc) getDisc(disc.getBarcode());
        //discs.remove(oldDisc);

        String newTitle = disc.getTitle().isEmpty()?oldDisc.getTitle():disc.getTitle();
        String newDirector = disc.getDirector().isEmpty()?oldDisc.getDirector():disc.getDirector();
        int newFsk = disc.getFsk() == -1?oldDisc.getFsk():disc.getFsk();

        Disc newDisc = new Disc(newTitle,oldDisc.getBarcode(),newDirector,newFsk);

        //discs.put(newDisc.getBarcode(), newDisc);
        hibernatePersistence.updateMedium(newDisc);


        return MediaServiceResult.OK;
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
        if(barcode.length() == 13) {
            final int[] ean13 = barcode.chars()
                    .map(x -> Character.getNumericValue(x))
                    .limit(12)
                    .toArray();

            return calculateCheckDigit(ean13) == Character.getNumericValue(barcode.charAt(12));
        }
        return false;
    }

    private int calculateCheckDigit(int[] digits) {
        int sum = 0;
        int multiplier = 3;
        for (int i = digits.length - 1; i >= 0; i--) {
        sum += digits[i] * multiplier;
        multiplier = (multiplier == 3) ? 1 : 3;
            }
        int sumPlus9 = sum + 9;
        int nextMultipleOfTen = sumPlus9 - (sumPlus9 % 10); // nextMultipleOfTen ist jetzt das nächste Vielfache von zehn
        return nextMultipleOfTen - sum;
    }

    /**
     * resetting the Maps for testing.
     */
    @Override
    public void clearMap() {
        //books.clear();
        //discs.clear();
        throw new UnsupportedOperationException("This method is no longer supported");
    }

    private String deleteDashesInIsbn(String isbn) {
        return isbn.replaceAll("-","");
    }
}
