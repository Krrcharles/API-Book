package fr.ensai.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class CountryTest {

    /**
     * DummyBook is a simple subclass of Book for testing purposes.
     * It implements the setCountry and getCountry methods so that we can verify
     * the behavior of addBook and removeBook in the Country class.
     */
    private static class DummyBook extends Book {
        private Country country;

        public DummyBook() {
            super();
        }

        @Override
        public void setCountry(Country country) {
            this.country = country;
        }

        @Override
        public Country getCountry() {
            return country;
        }
    }

    @Test
    void testDefaultConstructor() {
        Country country = new Country();
        assertNull(country.getCountryId(), "Default constructor should leave countryId as null");
        assertNull(country.getName(), "Default constructor should leave name as null");
        assertNotNull(country.getBooks(), "Books list should be initialized");
        assertTrue(country.getBooks().isEmpty(), "Books list should be empty");
    }

    @Test
    void testConstructorWithId() {
        Country country = new Country(1L, "France");
        assertEquals(1L, country.getCountryId(), "countryId should be set by the constructor");
        assertEquals("France", country.getName(), "Name should be set by the constructor");
    }

    @Test
    void testConstructorWithoutId() {
        Country country = new Country("Germany");
        assertNull(country.getCountryId(), "Convenience constructor should leave countryId as null");
        assertEquals("Germany", country.getName(), "Name should be set by the constructor");
    }

    @Test
    void testSetAndGetCountryId() {
        Country country = new Country("Test Country");
        country.setCountryId(10L);
        assertEquals(10L, country.getCountryId(), "setCountryId/getCountryId should work correctly");
    }

    @Test
    void testSetAndGetName() {
        Country country = new Country("Test Country");
        country.setName("Spain");
        assertEquals("Spain", country.getName(), "setName/getName should work correctly");
    }

    @Test
    void testSetAndGetBooks() {
        Country country = new Country("Test Country");
        List<Book> books = new ArrayList<>();
        DummyBook book1 = new DummyBook();
        DummyBook book2 = new DummyBook();
        books.add(book1);
        books.add(book2);
        country.setBooks(books);
        assertEquals(2, country.getBooks().size(), "Books list should contain two books");
        assertSame(book1, country.getBooks().get(0), "First book should match");
        assertSame(book2, country.getBooks().get(1), "Second book should match");
    }

    @Test
    void testAddBook() {
        Country country = new Country("Italy");
        DummyBook book = new DummyBook();
        country.addBook(book);
        assertEquals(1, country.getBooks().size(), "Books list should have one book after addBook");
        assertSame(country, book.getCountry(), "Book's country should be set when added");
    }

    @Test
    void testRemoveBook() {
        Country country = new Country("Italy");
        DummyBook book = new DummyBook();
        country.addBook(book);
        country.removeBook(book);
        assertTrue(country.getBooks().isEmpty(), "Books list should be empty after removeBook");
        assertNull(book.getCountry(), "Book's country should be null after removal");
    }

    @Test
    void testCountryToString() {
        Country country = new Country(3L, "USA");

        assertEquals(
                "Country(countryId=3, name=\"USA\")",
                country.toString(),
                "toString should return the expected string");
    }
}
