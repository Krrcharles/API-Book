package fr.ensai.demo.service;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.GenreRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class BookServiceTest {
    Logger LOG  = LoggerFactory.getLogger(BookServiceTest.class);

    @Autowired
    private BookService bookService;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private CountryRepository countryRepository;

    /*
    * Given a BookDTO that references an author name already in the database,
    * we expect the BookService to:
    *    - Find the existing author
    *    - Reuse that author (rather than creating a new one)
    *    - Save the book with the correct author linked
    *    - Return a BookDTO with the persisted bookId and the correct authorName
    * TODO : https://imgflip.com/i/9jnsei
    */
    @Test
    void testSaveBook_withExistingAuthor() {
        // GIVEN
        BookDto dto = new BookDto(42L, "Le Père Goriot (Updated)", 1835, "Honoré de Balzac", "Roman", "France");

        Author existingAuthor = new Author(3L, "Honoré de Balzac");
        // Return an iterable containing 1 author
        Iterable<Author> existingAuthors = List.of(existingAuthor);

        Mockito.when(authorRepository.findByName("Honoré de Balzac"))
                .thenReturn(existingAuthors);

        Genre genre = new Genre(1L, "Roman");
        Country country = new Country(1L, "France");
        Book savedMock = new Book(42L, existingAuthor, "Le Père Goriot (Updated)", 1835, genre, country);

        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedMock);

        // WHEN
        BookDto result = bookService.saveBook(dto);

        // THEN
        Mockito.verify(authorRepository).findByName("Honoré de Balzac");
        Mockito.verify(bookRepository).save(any(Book.class));

        assertNotNull(result, "Resulting BookDTO should not be null");
        assertEquals(42L, result.getBookId());
        assertEquals("Le Père Goriot (Updated)", result.getTitle());
        assertEquals("Honoré de Balzac", result.getAuthorName());
    }



    /*
    * Given a BookDTO that references an author name not in the database,
    * we expect the BookService to:
    *    - Detect that the author does not exist
    *    - Create a new Author entity
    *    - Save the new author
    *    - Save the book linked to that newly created author
    *    - Return a BookDTO with the persisted bookId and the authorName matching the input
    * TODO : https://imgflip.com/i/9jnsei
    */
    @Test
    void testSaveBook_withNewAuthor() {
        // GIVEN
        BookDto dto = new BookDto();
        dto.setBookId(null);
        dto.setTitle("New Book Title");
        dto.setPublicationYear(2025);
        dto.setAuthorName("New Author Name");

        // Return empty list => no author found
        Iterable<Author> noAuthors = List.of();
        Mockito.when(authorRepository.findByName("New Author Name")).thenReturn(noAuthors);

        // After creating a new Author, let's say it has ID 99
        Author newAuthor = new Author(99L, "New Author Name");
        Mockito.when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

        Book savedMock = new Book(100L, newAuthor, "New Book Title", 2025, null, null);

        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedMock);

        // WHEN
        BookDto result = bookService.saveBook(dto);

        // THEN
        Mockito.verify(authorRepository).findByName("New Author Name");
        // We expect to save a new author
        Mockito.verify(authorRepository).save(any(Author.class));
        // We expect to save a new book
        Mockito.verify(bookRepository).save(any(Book.class));

        assertNotNull(result, "Resulting BookDTO should not be null");
        assertEquals(100L, result.getBookId());
        assertEquals("New Author Name", result.getAuthorName());
    }
}


