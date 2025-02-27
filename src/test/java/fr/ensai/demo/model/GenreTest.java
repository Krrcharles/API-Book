package fr.ensai.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class GenreTest {

    /**
     * DummyBook is a simple subclass of Book for testing purposes.
     * It implements the setGenre and getGenre methods so that we can verify
     * the behavior of addBook and removeBook in the Genre class.
     */
    private static class DummyBook extends Book {
        private Genre genre;

        public DummyBook() {
            super();
        }

        @Override
        public void setGenre(Genre genre) {
            this.genre = genre;
        }

        @Override
        public Genre getGenre() {
            return genre;
        }
    }

    @Test
    void testDefaultConstructor() {
        Genre genre = new Genre();
        assertNull(genre.getGenreId(), "Default constructor should leave genreId as null");
        assertNull(genre.getLabel(), "Default constructor should leave label as null");
        assertNotNull(genre.getBooks(), "Books list should be initialized");
        assertTrue(genre.getBooks().isEmpty(), "Books list should be empty");
    }

    @Test
    void testConstructorWithId() {
        Genre genre = new Genre(1L, "Drama");
        assertEquals(1L, genre.getGenreId(), "GenreId should be set by the constructor");
        assertEquals("Drama", genre.getLabel(), "Label should be set by the constructor");
    }

    @Test
    void testConstructorWithoutId() {
        Genre genre = new Genre("Novel");
        assertNull(genre.getGenreId(), "Convenience constructor should leave genreId as null");
        assertEquals("Novel", genre.getLabel(), "Label should be set by the constructor");
    }

    @Test
    void testSetAndGetGenreId() {
        Genre genre = new Genre("Test");
        genre.setGenreId(10L);
        assertEquals(10L, genre.getGenreId(), "setGenreId/getGenreId should work correctly");
    }

    @Test
    void testSetAndGetLabel() {
        Genre genre = new Genre("Test");
        genre.setLabel("Novel");
        assertEquals("Novel", genre.getLabel(), "setLabel/getLabel should work correctly");
    }

    @Test
    void testSetAndGetBooks() {
        Genre genre = new Genre("Test Genre");
        List<Book> books = new ArrayList<>();
        DummyBook book1 = new DummyBook();
        DummyBook book2 = new DummyBook();
        books.add(book1);
        books.add(book2);
        genre.setBooks(books);
        assertEquals(2, genre.getBooks().size(), "Books list should contain two books");
        assertSame(book1, genre.getBooks().get(0), "First book should match");
        assertSame(book2, genre.getBooks().get(1), "Second book should match");
    }

    @Test
    void testAddBook() {
        Genre genre = new Genre("Novel");
        DummyBook book = new DummyBook();
        genre.addBook(book);
        assertEquals(1, genre.getBooks().size(), "Books list should have one book after addBook");
        assertSame(genre, book.getGenre(), "Book's genre should be set when added");
    }

    @Test
    void testRemoveBook() {
        Genre genre = new Genre("Novel");
        DummyBook book = new DummyBook();
        genre.addBook(book);
        genre.removeBook(book);
        assertTrue(genre.getBooks().isEmpty(), "Books list should be empty after removeBook");
        assertNull(book.getGenre(), "Book's genre should be null after removal");
    }

    @Test
    void testGenreToString() {
        Genre genre = new Genre(2L, "Fiction");

        assertEquals(
                "Genre(genreId=2, label=\"Fiction\")",
                genre.toString(),
                "toString should return the expected string");
    }
}
