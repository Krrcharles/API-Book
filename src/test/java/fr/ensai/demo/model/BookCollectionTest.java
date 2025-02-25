package fr.ensai.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

class BookCollectionTest {

    /**
     * Helper method to create a dummy Book with a given id.
     * It uses simple instances of Author, Genre, and Country.
     */
    private Book createDummyBook(Long id) {
        Author author = new Author("Dummy Author");
        Genre genre = new Genre("Dummy Genre");
        Country country = new Country("Dummy Country");
        Book book = new Book(author, "Dummy Title", 2021, genre, country);
        book.setBookId(id);
        return book;
    }

    @Test
    void testDefaultConstructor() {
        // The default (protected) constructor is accessible in the same package.
        BookCollection bc = new BookCollection();
        assertNull(bc.getCollectionId(), "Default constructor should leave collectionId as null");
        assertNull(bc.getName(), "Default constructor should leave name as null");
        assertNull(bc.getDistanceJaro(), "Default constructor should leave distanceJaro as null");
        assertNull(bc.getDistanceJaccard(), "Default constructor should leave distanceJaccard as null");
        assertNotNull(bc.getBooks(), "Books list should be initialized");
        assertTrue(bc.getBooks().isEmpty(), "Books list should be empty by default");
    }

    @Test
    void testConstructorWithId() {
        BookCollection bc = new BookCollection(1L, "My Collection", 0.75, 0.85);
        assertEquals(1L, bc.getCollectionId(), "collectionId should be set by the constructor");
        assertEquals("My Collection", bc.getName(), "Name should be set by the constructor");
        assertEquals(0.75, bc.getDistanceJaro(), "distanceJaro should be set by the constructor");
        assertEquals(0.85, bc.getDistanceJaccard(), "distanceJaccard should be set by the constructor");
        assertNotNull(bc.getBooks(), "Books list should be initialized");
        assertTrue(bc.getBooks().isEmpty(), "Books list should be empty");
    }

    @Test
    void testConstructorWithoutId() {
        BookCollection bc = new BookCollection("Another Collection", 0.65, 0.80);
        assertNull(bc.getCollectionId(), "Convenience constructor should leave collectionId as null");
        assertEquals("Another Collection", bc.getName(), "Name should be set by the constructor");
        assertEquals(0.65, bc.getDistanceJaro(), "distanceJaro should be set by the constructor");
        assertEquals(0.80, bc.getDistanceJaccard(), "distanceJaccard should be set by the constructor");
        assertNotNull(bc.getBooks(), "Books list should be initialized");
        assertTrue(bc.getBooks().isEmpty(), "Books list should be empty");
    }

    @Test
    void testSettersAndGetters() {
        BookCollection bc = new BookCollection();
        bc.setCollectionId(10L);
        bc.setName("Test Collection");
        bc.setDistanceJaro(0.55);
        bc.setDistanceJaccard(0.60);
        
        List<Book> books = new ArrayList<>();
        Book book1 = createDummyBook(100L);
        Book book2 = createDummyBook(101L);
        books.add(book1);
        books.add(book2);
        bc.setBooks(books);
        
        assertEquals(10L, bc.getCollectionId(), "setCollectionId/getCollectionId not working correctly");
        assertEquals("Test Collection", bc.getName(), "setName/getName not working correctly");
        assertEquals(0.55, bc.getDistanceJaro(), "setDistanceJaro/getDistanceJaro not working correctly");
        assertEquals(0.60, bc.getDistanceJaccard(), "setDistanceJaccard/getDistanceJaccard not working correctly");
        assertEquals(2, bc.getBooks().size(), "Books list should contain two books");
        assertSame(book1, bc.getBooks().get(0), "First book should match");
        assertSame(book2, bc.getBooks().get(1), "Second book should match");
    }

    @Test
    void testGetBookIds() {
        BookCollection bc = new BookCollection("Collection", 0.9, 0.95);
        Book book1 = createDummyBook(200L);
        Book book2 = createDummyBook(201L);
        bc.addBook(book1);
        bc.addBook(book2);
        List<Long> bookIds = bc.getBookIds();
        assertEquals(2, bookIds.size(), "getBookIds should return the correct number of IDs");
        assertTrue(bookIds.contains(200L), "getBookIds should contain the id 200L");
        assertTrue(bookIds.contains(201L), "getBookIds should contain the id 201L");
    }

    @Test
    void testAddBook() {
        BookCollection bc = new BookCollection("Collection", 0.8, 0.85);
        Book book = createDummyBook(300L);
        bc.addBook(book);
        assertEquals(1, bc.getBooks().size(), "Books list should have one book after addBook");
        // Adding the same book again should not duplicate it
        bc.addBook(book);
        assertEquals(1, bc.getBooks().size(), "Duplicate addBook should not increase the list size");
        // Add another book
        Book book2 = createDummyBook(301L);
        bc.addBook(book2);
        assertEquals(2, bc.getBooks().size(), "Books list should have two books after adding a new one");
    }

    @Test
    void testRemoveBook() {
        BookCollection bc = new BookCollection("Collection", 0.8, 0.85);
        Book book = createDummyBook(400L);
        bc.addBook(book);
        assertEquals(1, bc.getBooks().size(), "Books list should have one book before removal");
        bc.removeBook(book);
        assertTrue(bc.getBooks().isEmpty(), "Books list should be empty after removeBook");
    }
}
