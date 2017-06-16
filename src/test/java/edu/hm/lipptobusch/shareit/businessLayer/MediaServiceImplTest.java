package edu.hm.lipptobusch.shareit.businessLayer;

import edu.hm.lipptobusch.shareit.models.Book;
import edu.hm.lipptobusch.shareit.models.Disc;
import edu.hm.lipptobusch.shareit.models.Medium;
import edu.hm.lipptobusch.shareit.persistence.HibernatePersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Maximilian Lipp, lipp@hm.edu
 * @author Florian Tobusch, tobusch@hm.edu
 * @version 2017-04-19
 */
public class MediaServiceImplTest {
    private MediaServiceImpl mediaService;
    private HibernatePersistence hibernatePersistenceMock;
    private static final HashMap<String, Book> books = new HashMap<>();
    private static final HashMap<String, Disc> discs = new HashMap<>();

    @Before
    public void initialize(){
        books.clear();
        discs.clear();

        hibernatePersistenceMock = mock(HibernatePersistence.class);
        mediaService = new MediaServiceImpl(hibernatePersistenceMock);

        //some mocks for methods of the HibernatePersistence-Class
        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) {
                        Book book = (Book) invocation.getArgument(0);
                        books.put(book.getIsbn(),book);
                        return null;
                    }
                }
        ).when(hibernatePersistenceMock).addMedium(any(Book.class));

        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) {
                        Book book = (Book) invocation.getArgument(0);
                        books.remove(book.getIsbn());
                        books.put(book.getIsbn(),book);
                        return null;
                    }
                }
        ).when(hibernatePersistenceMock).updateMedium(any(Book.class));

        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) {
                        Disc disc = (Disc) invocation.getArgument(0);
                        discs.put(disc.getBarcode(),disc);
                        return null;
                    }
                }
        ).when(hibernatePersistenceMock).addMedium(any(Disc.class));

        doAnswer(
                new Answer<Void>() {
                    public Void answer(InvocationOnMock invocation) {
                        Disc disc = (Disc) invocation.getArgument(0);
                        discs.remove(disc.getBarcode());
                        discs.put(disc.getBarcode(),disc);
                        return null;
                    }
                }
        ).when(hibernatePersistenceMock).updateMedium(any(Disc.class));
    }

    //----------------------------------------------
    //          Tests for addBook()
    //----------------------------------------------
    @Test
    public void testAddBookSuccessful() {
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(1, allBooks.length);
        assertEquals(bookToAdd, allBooks[0]);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddBookSuccessfulWithLines() {
        //arrange
        Book bookToAdd = new Book("title", "autor", "978-3-446-42150-9");
        Book bookExpected = new Book("title", "autor", "9783446421509");
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(1, allBooks.length);
        assertEquals(bookExpected, allBooks[0]);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddBookWithDuplicateIsbn(){
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.DUPLICATE_ISBN;


        //act
        mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(1, allBooks.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testAddBookWithInvalidIsbn(){
        //arrange
        Book bookToAdd = new Book("title", "autor", "9783826801929");
        MediaServiceResult expected = MediaServiceResult.INVALID_ISBN;


        //act
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testAddBookWithIncompleteArgumentsBecauseOfMissingAuthor(){
        //arrange
        Book bookToAdd = new Book("title", "", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testAddBookWithIncompleteArgumentsBecauseOfMissingTitle(){
        //arrange
        Book bookToAdd = new Book("", "author", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        MediaServiceResult actual = mediaService.addBook(bookToAdd);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(0, allBooks.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    //----------------------------------------------
    //          Tests for getBooks()
    //----------------------------------------------

    @Test
    public void testGetBooksAddNoBookAndGetEmptyArray() {
        //arrange

        //act
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(0, allBooks.length);
    }

    @Test
    public void testGetBooksAddTwoBooksSuccessful(){
        //arrange
        Book bookOne = new Book("title", "autor", "9783866801929");
        Book bookTwo = new Book("title1", "autor1", "9783868838688");

        //act
        mediaService.addBook(bookOne);
        mediaService.addBook(bookTwo);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium[] allBooks = mediaService.getBooks();

        //assert
        assertEquals(2, allBooks.length);
        assertEquals(bookOne,allBooks[0]);
        assertEquals(bookTwo,allBooks[1]);
    }

    //----------------------------------------------
    //          Tests for getBook()
    //----------------------------------------------
    @Test
    public void testGetBook(){
        //arrange
        Book bookOne = new Book("title", "autor", "9783866801929");

        //act
        mediaService.addBook(bookOne);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium actual = mediaService.getBook("9783866801929");

        //assert
        assertEquals(bookOne,actual);
    }
    @Test
    public void testGetBookWithSlashes(){
        //arrange
        Book bookOne = new Book("title", "autor", "978-3-446-42150-9");
        Book bookExpected = new Book("title", "autor", "9783446421509");

        //act
        mediaService.addBook(bookOne);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium actual = mediaService.getBook("978-3-446-42150-9");

        //assert
        assertEquals(bookExpected,actual);
    }

    @Test
    public void testGetBookIsbnDoesNotExist(){
        //arrange
        Book bookOne = new Book("title", "autor", "9783866801929");

        //act
        mediaService.addBook(bookOne);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium actual = mediaService.getBook("9783866801921");

        //assert
        assertEquals(null, actual);
    }

    //----------------------------------------------
    //          Tests for updateBook()
    //----------------------------------------------
    @Test
    public void testUpdateBookWithUpdatingTitle(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "9783866801929");
        Book bookWithModifications = new Book("title2", "", "9783866801929");
        Book bookExpected = new Book("title2", "autor", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addBook(bookToUpdate);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "9783866801929");
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium updatedBook = mediaService.getBook("9783866801929");

        //assert
        assertEquals(bookExpected,updatedBook);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateBookWithUpdatingTitleAndSlashesInIsbn(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "978-3-446-42150-9");
        Book bookWithModifications = new Book("title2", "", "978-3-446-42150-9");
        Book bookExpected = new Book("title2", "autor", "9783446421509");
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addBook(bookToUpdate);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "978-3-446-42150-9");
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium updatedBook = mediaService.getBook("978-3-446-42150-9");

        //assert
        assertEquals(bookExpected,updatedBook);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testUpdateBookWithUpdatingAuthor(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "9783866801929");
        Book bookWithModifications = new Book("", "autor2", "9783866801929");
        Book bookExpected = new Book("title", "autor2", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addBook(bookToUpdate);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "9783866801929");
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        Medium updatedBook = mediaService.getBook("9783866801929");

        //assert
        assertEquals(bookExpected,updatedBook);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testUpdateBookModifyingIsbnNotAllowed(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "9783866801929");
        Book bookWithModifications = new Book("title", "autor", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.MODIFYING_ISBN_NOT_ALLOWED;

        //act
        mediaService.addBook(bookToUpdate);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "9783866801923");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testUpdateBookIsbnNotFound(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "9783866801929");
        Book bookWithModifications = new Book("title", "autor", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.ISBN_NOT_FOUND;

        //act
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "9783866801929");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void testUpdateBookIncompleteArguments(){
        //arrange
        Book bookToUpdate = new Book("title", "autor", "9783866801929");
        Book bookWithModifications = new Book("", "", "9783866801929");
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        mediaService.addBook(bookToUpdate);
        when(hibernatePersistenceMock.getTable(Book.class)).thenReturn(new ArrayList<Medium>(books.values()));
        MediaServiceResult actual = mediaService.updateBook(bookWithModifications, "9783866801929");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    //----------------------------------------------
    //          Tests for addDisc()
    //----------------------------------------------
    @Test
    public void testAddDiscSuccessful() {
        //arrange
        Disc discToAdd = new Disc("title", "7010745910963", "director",12);
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        MediaServiceResult actual = mediaService.addDisc(discToAdd);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(1, allDiscs.length);
        assertEquals(discToAdd, allDiscs[0]);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddDiscDuplicateBarcode() {
        //arrange
        Disc discToAdd = new Disc("title", "7010745910963", "director",12);
        Disc discWithError = new Disc("title2", "7010745910963", "director2",16);
        MediaServiceResult expected = MediaServiceResult.DUPLICATE_Barcode;

        //act
        mediaService.addDisc(discToAdd);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.addDisc(discWithError);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(1, allDiscs.length);
        assertEquals(discToAdd, allDiscs[0]);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddDiscIncompleteArgumentsBecauseOfMissingDirector() {
        //arrange
        Disc discToAdd = new Disc("title", "7010745910963", "",12);
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        MediaServiceResult actual = mediaService.addDisc(discToAdd);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(0, allDiscs.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddDiscIncompleteArgumentsBecauseOfMissingTitle() {
        //arrange
        Disc discToAdd = new Disc("", "7010745910963", "director",12);
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        MediaServiceResult actual = mediaService.addDisc(discToAdd);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(0, allDiscs.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testAddDiscInvalidBarcode() {
        //arrange
        Disc discToAdd = new Disc("title", "", "director",12);
        MediaServiceResult expected = MediaServiceResult.INVALID_BARCODE;

        //act
        MediaServiceResult actual = mediaService.addDisc(discToAdd);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(0, allDiscs.length);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    //----------------------------------------------
    //          Tests for getDiscs()
    //----------------------------------------------
    @Test
    public void testGetDiscsAddNoDiscAndGetEmptyArray() {
        //arrange

        //act
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(0, allDiscs.length);
    }
    @Test
    public void testGetDiscsAddTwoDiscsSuccessful() {
        //arrange
        Disc discOne = new Disc("title", "0123456789012", "director",12);
        Disc discTwo = new Disc("title2", "1000200100103", "director2",16);
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addDisc(discOne);
        MediaServiceResult actual = mediaService.addDisc(discTwo);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium[] allDiscs = mediaService.getDiscs();

        //assert
        assertEquals(2, allDiscs.length);
        assertEquals(discOne, allDiscs[0]);
        assertEquals(discTwo, allDiscs[1]);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    //----------------------------------------------
    //          Tests for getDisc()
    //----------------------------------------------
    @Test
    public void testGetDisc(){
        //arrange
        Disc discOne = new Disc("title", "1000200100103", "director",12);

        //act
        mediaService.addDisc(discOne);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium actual = mediaService.getDisc("1000200100103");

        //assert
        assertEquals(discOne,actual);
    }

    @Test
    public void testGetDiscBarcodeDoesNotExist(){
        //arrange
        Disc discOne = new Disc("title", "1000200100103", "director",12);

        //act
        mediaService.addDisc(discOne);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium actual = mediaService.getDisc("12345662");

        //assert
        assertEquals(null, actual);
    }
    //----------------------------------------------
    //          Tests for updateDisc()
    //----------------------------------------------
    @Test
    public void testUpdateDiscUpdatingTitle() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("title2", "1000200100103", "",12);
        Disc discExptected = new Disc("title2", "1000200100103", "director",12);
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium updatedDisc = mediaService.getDisc("1000200100103");

        //assert
        assertEquals(discExptected, updatedDisc);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateDiscUpdatingDirector() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("", "1000200100103", "director2",12);
        Disc discExptected = new Disc("title", "1000200100103", "director2",12);
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium updatedDisc = mediaService.getDisc("1000200100103");

        //assert
        assertEquals(discExptected, updatedDisc);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateDiscUpdatingFsk() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("", "1000200100103", "",18);
        Disc discExptected = new Disc("title", "1000200100103", "director",18);
        MediaServiceResult expected = MediaServiceResult.OK;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        Medium updatedDisc = mediaService.getDisc("1000200100103");

        //assert
        assertEquals(discExptected, updatedDisc);
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateDiscModifiyingBarcodeNotAllwoed() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("", "0123456789012", "",18);
        MediaServiceResult expected = MediaServiceResult.MODIFYING_BARCODE_NOT_ALLOWED;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateDiscBarcodeNotFound() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("", "0123456789012", "",18);
        MediaServiceResult expected = MediaServiceResult.MODIFYING_BARCODE_NOT_ALLOWED;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
    @Test
    public void testUpdateDiscIncompleteArguments() {
        //arrange
        Disc discToUpdate = new Disc("title", "1000200100103", "director",12);
        Disc discWithModifications = new Disc("", "1000200100103", "",-1);
        MediaServiceResult expected = MediaServiceResult.INCOMPLETE_ARGUMENTS;

        //act
        mediaService.addDisc(discToUpdate);
        when(hibernatePersistenceMock.getTable(Disc.class)).thenReturn(new ArrayList<Medium>(discs.values()));
        MediaServiceResult actual = mediaService.updateDisc(discWithModifications, "1000200100103");

        //assert
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
}