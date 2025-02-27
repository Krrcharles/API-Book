package fr.ensai.demo.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BookDtoTest {

    @Test
    void testDefaultConstructorAndSetters() {
        BookDto dto = new BookDto();
        dto.setBookId(100L);
        dto.setTitle("Effective Java");
        dto.setPublicationYear(2008);
        dto.setAuthorName("Joshua Bloch");
        dto.setGenreLabel("Programming");
        dto.setCountryName("USA");

        assertEquals(100L, dto.getBookId(), "BookId should be 100L");
        assertEquals("Effective Java", dto.getTitle(), "Title should be 'Effective Java'");
        assertEquals(2008, dto.getPublicationYear(), "Year should be 2008");
        assertEquals("Joshua Bloch", dto.getAuthorName(), "Author name should be 'Joshua Bloch'");
        assertEquals("Programming", dto.getGenreLabel(), "Genre label should be 'Programming'");
        assertEquals("USA", dto.getCountryName(), "Country name should be 'USA'");
    }

    @Test
    void testConstructorWithId() {
        BookDto dto = new BookDto(101L, "Clean Code", 2008, "Robert C. Martin", "Programming", "USA");

        assertEquals(101L, dto.getBookId(), "BookId should be 101L");
        assertEquals("Clean Code", dto.getTitle(), "Title should be 'Clean Code'");
        assertEquals(2008, dto.getPublicationYear(), "Year should be 2008");
        assertEquals("Robert C. Martin", dto.getAuthorName(), "Author name should be 'Robert C. Martin'");
        assertEquals("Programming", dto.getGenreLabel(), "Genre label should be 'Programming'");
        assertEquals("USA", dto.getCountryName(), "Country name should be 'USA'");
    }

    @Test
    void testConstructorWithoutId() {
        BookDto dto = new BookDto("Refactoring", 1999, "Martin Fowler", "Programming", "USA");

        assertNull(dto.getBookId(), "BookId should be null when not provided");
        assertEquals("Refactoring", dto.getTitle(), "Title should be 'Refactoring'");
        assertEquals(1999, dto.getPublicationYear(), "Year should be 1999");
        assertEquals("Martin Fowler", dto.getAuthorName(), "Author name should be 'Martin Fowler'");
        assertEquals("Programming", dto.getGenreLabel(), "Genre label should be 'Programming'");
        assertEquals("USA", dto.getCountryName(), "Country name should be 'USA'");
    }

    @Test
    void testBookDtoToString() {
        BookDto bookDto = new BookDto(10L, "Sample Book", 2020, "Test Author", "Fiction", "USA");

        assertEquals(
                "BookDto(bookId=10, title=\"Sample Book\", publicationYear=2020, authorName=\"Test Author\", genreLabel=\"Fiction\", countryName=\"USA\")",
                bookDto.toString(),
                "toString should return the expected string");
    }
}
