package fr.ensai.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void testConstructorWithId() {
        Author author = new Author("John Doe");
        Genre genre = new Genre("Mystery");
        Country country = new Country("USA");
        Book book = new Book(1L, author, "The Mystery", 2020, genre, country);

        assertEquals(1L, book.getBookId(), "BookId should be set by the constructor");
        assertEquals("The Mystery", book.getTitle(), "Title should be set by the constructor");
        assertEquals(2020, book.getPublicationYear(), "Year should be set by the constructor");
        assertSame(author, book.getAuthor(), "Author should be set by the constructor");
        assertSame(genre, book.getGenre(), "Genre should be set by the constructor");
        assertSame(country, book.getCountry(), "Country should be set by the constructor");
    }

    @Test
    void testConstructorWithoutId() {
        Author author = new Author("Jane Doe");
        Genre genre = new Genre("Romance");
        Country country = new Country("UK");
        Book book = new Book(author, "Love Story", 2019, genre, country);

        assertNull(book.getBookId(), "BookId should be null when not provided");
        assertEquals("Love Story", book.getTitle(), "Title should be set by the constructor");
        assertEquals(2019, book.getPublicationYear(), "Year should be set by the constructor");
        assertSame(author, book.getAuthor(), "Author should be set by the constructor");
        assertSame(genre, book.getGenre(), "Genre should be set by the constructor");
        assertSame(country, book.getCountry(), "Country should be set by the constructor");
    }

    @Test
    void testSetAndGetProperties() {
        Book book = new Book(1L, null, "Test title", 0, null, null);
        Author author = new Author("Alice");
        Genre genre = new Genre("Sci-Fi");
        Country country = new Country("Canada");

        book.setBookId(100L);
        book.setTitle("Future World");
        book.setPublicationYear(2050);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setCountry(country);

        assertEquals(100L, book.getBookId(), "BookId getter/setter not working correctly");
        assertEquals("Future World", book.getTitle(), "Title getter/setter not working correctly");
        assertEquals(2050, book.getPublicationYear(), "Year getter/setter not working correctly");
        assertSame(author, book.getAuthor(), "Author getter/setter not working correctly");
        assertSame(genre, book.getGenre(), "Genre getter/setter not working correctly");
        assertSame(country, book.getCountry(), "Country getter/setter not working correctly");
    }

    @Test
    void testEqualsSameInstance() {
        Author author = new Author("Alice");
        Genre genre = new Genre("Sci-Fi");
        Country country = new Country("Canada");
        Book book = new Book(author, "Future World", 2050, genre, country);

        // Same instance should be equal
        assertTrue(book.equals(book), "A book should be equal to itself");
    }

    @Test
    void testEqualsEqualObjects() {
        Author author = new Author("Bob");
        Genre genre = new Genre("Horror");
        Country country = new Country("USA");
        Book book1 = new Book(author, "Nightmare", 1999, genre, country);
        Book book2 = new Book(author, "Nightmare", 1999, genre, country);

        assertTrue(book1.equals(book2), "Books with identical properties should be equal");
        assertTrue(book2.equals(book1), "Equality should be symmetric");
    }

    @Test
    void testEqualsDifferentObjects() {
        Author author1 = new Author("Bob");
        Author author2 = new Author("Charlie");
        Genre genre = new Genre("Horror");
        Country country = new Country("USA");

        Book book1 = new Book(author1, "Nightmare", 1999, genre, country);
        // Different author
        Book book2 = new Book(author2, "Nightmare", 1999, genre, country);
        assertFalse(book1.equals(book2), "Books with different authors should not be equal");

        // Different title
        Book book3 = new Book(author1, "Dreams", 1999, genre, country);
        assertFalse(book1.equals(book3), "Books with different titles should not be equal");

        // Different year
        Book book4 = new Book(author1, "Nightmare", 2000, genre, country);
        assertFalse(book1.equals(book4), "Books with different years should not be equal");

        // Different genre
        Genre differentGenre = new Genre("Comedy");
        Book book5 = new Book(author1, "Nightmare", 1999, differentGenre, country);
        assertFalse(book1.equals(book5), "Books with different genres should not be equal");

        // Different country
        Country differentCountry = new Country("UK");
        Book book6 = new Book(author1, "Nightmare", 1999, genre, differentCountry);
        assertFalse(book1.equals(book6), "Books with different countries should not be equal");
    }

    @Test
    void testEqualsNullAndDifferentType() {
        Author author = new Author("Alice");
        Genre genre = new Genre("Sci-Fi");
        Country country = new Country("Canada");
        Book book = new Book(author, "Future World", 2050, genre, country);

        assertFalse(book.equals(null), "A book should not be equal to null");
        assertFalse(book.equals("Some String"), "A book should not be equal to an object of a different type");
    }

    @Test
    void testBookToString() {
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(2L, "Fiction");
        Country country = new Country(3L, "USA");
        Book book = new Book(10L, author, "Sample Book", 2020, genre, country);

        assertEquals(
                "Book(bookId=10, title=\"Sample Book\", publicationYear=2020, author=Author(authorId=1, name=\"Test Author\"), genre=Genre(genreId=2, label=\"Fiction\"), country=Country(countryId=3, name=\"USA\"))",
                book.toString(),
                "toString should return the expected string");
    }
}
