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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

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

@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class BookServiceTest {
    Logger LOG = LoggerFactory.getLogger(BookServiceTest.class);

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

    /**
     * Test for creating a book when the author, genre, and country do not exist yet.
     * The BookService should:
     * - Not find an existing author, genre, or country.
     * - Create and save the new entities.
     * - Save the book and return a BookDto with an assigned bookId.
     */
    @Test
    void testCreateBook_withNewAuthor() {
        // GIVEN
        BookDto dto = new BookDto(null, "New Book", 2025, "New Author", "New Genre", "New Country");

        // Simulate that no author exists
        Mockito.when(authorRepository.findByName("New Author")).thenReturn(List.of());
        // Simulate creating a new author
        Author newAuthor = new Author(1L, "New Author");
        Mockito.when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

        // Simulate that no genre exists
        Mockito.when(genreRepository.findByLabel("New Genre")).thenReturn(List.of());
        Genre newGenre = new Genre(1L, "New Genre");
        Mockito.when(genreRepository.save(any(Genre.class))).thenReturn(newGenre);

        // Simulate that no country exists
        Mockito.when(countryRepository.findByName("New Country")).thenReturn(List.of());
        Country newCountry = new Country(1L, "New Country");
        Mockito.when(countryRepository.save(any(Country.class))).thenReturn(newCountry);

        // Simulate saving the book and returning a book with an ID
        Book savedBook = new Book(1L, newAuthor, "New Book", 2025, newGenre, newCountry);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // WHEN
        BookDto result = bookService.createBook(dto);

        // THEN
        assertNotNull(result, "The resulting BookDto should not be null");
        assertEquals(1L, result.getBookId());
        assertEquals("New Book", result.getTitle());
        assertEquals("New Author", result.getAuthorName());
        assertEquals("New Genre", result.getGenreLabel());
        assertEquals("New Country", result.getCountryName());
    }

    /**
     * Test for updating an existing book.
     * The BookService should:
     * - Find the existing book by its ID in the database.
     * - Update the book's information (title, publication year, etc.).
     * - Save and return the updated BookDto.
     */
    @Test
    void testUpdateBook_withExistingBook() {
        // GIVEN
        BookDto dto = new BookDto(1L, "Updated Book", 2025, "Existing Author", "Existing Genre", "Existing Country");

        // Simulate an existing author
        Author existingAuthor = new Author(2L, "Existing Author");
        Mockito.when(authorRepository.findByName("Existing Author")).thenReturn(List.of(existingAuthor));

        // Simulate an existing genre
        Genre existingGenre = new Genre(2L, "Existing Genre");
        Mockito.when(genreRepository.findByLabel("Existing Genre")).thenReturn(List.of(existingGenre));

        // Simulate an existing country
        Country existingCountry = new Country(2L, "Existing Country");
        Mockito.when(countryRepository.findByName("Existing Country")).thenReturn(List.of(existingCountry));

        // Simulate that the book already exists in the database
        Book existingBook = new Book(1L, existingAuthor, "Old Title", 2020, existingGenre, existingCountry);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        // Simulate saving the updated book
        Book updatedBook = new Book(1L, existingAuthor, "Updated Book", 2025, existingGenre, existingCountry);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // WHEN
        BookDto result = bookService.updateBook(dto);

        // THEN
        assertNotNull(result, "The resulting BookDto should not be null");
        assertEquals(1L, result.getBookId());
        assertEquals("Updated Book", result.getTitle());
        assertEquals("Existing Author", result.getAuthorName());
    }

    /**
     * Test for deleting an existing book.
     * The BookService should:
     * - Find the book by its ID.
     * - Delete the book and return true.
     */
    // @Test
    void testDeleteBook_whenBookExists() {
        // GIVEN
        Long bookId = 1L;
        Book existingBook = new Book(bookId, new Author(2L, "Existing Author"), "Title", 2020,
                new Genre(2L, "Genre"), new Country(2L, "Country"));
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        // WHEN
        boolean result = bookService.deleteBook(bookId);

        // THEN
        Mockito.verify(bookRepository).deleteById(bookId);
        assertTrue(result, "Deleting an existing book should return true");
    }

    /**
     * Test for deleting a non-existing book.
     * The BookService should return false if the book does not exist.
     */
    @Test
    void testDeleteBook_whenBookDoesNotExist() {
        // GIVEN
        Long bookId = 99L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // WHEN
        boolean result = bookService.deleteBook(bookId);

        // THEN
        assertFalse(result, "Deleting a non-existing book should return false");
    }

    /**
     * Test for retrieving all books.
     * The BookService should return a collection of BookDto corresponding to the persisted books.
     */
    @Test
    void testGetBooks_whenBooksExist() {
        // GIVEN
        Author author = new Author(1L, "Author");
        Genre genre = new Genre(1L, "Genre");
        Country country = new Country(1L, "Country");
        Book book1 = new Book(1L, author, "Title1", 2020, genre, country);
        Book book2 = new Book(2L, author, "Title2", 2021, genre, country);
        List<Book> books = List.of(book1, book2);
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        // WHEN
        Iterable<BookDto> result = bookService.getBooks();

        // THEN
        assertNotNull(result, "The result should not be null");
        List<BookDto> resultList = (List<BookDto>) result;
        assertEquals(2, resultList.size(), "The number of books returned should be 2");
    }

    /**
     * Test for retrieving an existing book by its ID.
     * The BookService should return the corresponding BookDto.
     */
    @Test
    void testGetBookById_whenBookExists() {
        // GIVEN
        Long bookId = 1L;
        Author author = new Author(1L, "Author");
        Genre genre = new Genre(1L, "Genre");
        Country country = new Country(1L, "Country");
        Book book = new Book(bookId, author, "Title", 2020, genre, country);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // WHEN
        BookDto result = bookService.getBookById(bookId);

        // THEN
        assertNotNull(result, "The resulting BookDto should not be null");
        assertEquals(bookId, result.getBookId(), "The book ID should match");
        assertEquals("Title", result.getTitle(), "The book title should match");
    }

    /**
     * Test for retrieving a book by a non-existing ID.
     * The BookService should throw an IllegalArgumentException.
     */
    @Test
    void testGetBookById_whenBookDoesNotExist() {
        // GIVEN
        Long bookId = 99L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(bookId);
        }, "An IllegalArgumentException should be thrown if the book does not exist");
    }

    /*
    * Given a BookDTO that references an author name already in the database,
    * we expect the BookService to:
    *    - Find the existing author
    *    - Reuse that author (rather than creating a new one)
    *    - Save the book with the correct author linked
    *    - Return a BookDTO with the persisted bookId and the correct authorName
    * TODO : https://imgflip.com/i/9jnsei
    */
    // @Test
    // void testSaveBook_withExistingAuthor() {
    //     // GIVEN
    //     BookDto dto = new BookDto(42L, "Le Père Goriot (Updated)", 1835, "Honoré de Balzac", "Roman", "France");

    //     Author existingAuthor = new Author(3L, "Honoré de Balzac");
    //     // Return an iterable containing 1 author
    //     Iterable<Author> existingAuthors = List.of(existingAuthor);

    //     Mockito.when(authorRepository.findByName("Honoré de Balzac"))
    //             .thenReturn(existingAuthors);

    //     Genre genre = new Genre(1L, "Roman");
    //     Country country = new Country(1L, "France");
    //     Book savedMock = new Book(42L, existingAuthor, "Le Père Goriot (Updated)", 1835, genre, country);

    //     Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedMock);

    //     // WHEN
    //     BookDto result = bookService.saveBook(dto);

    //     // THEN
    //     Mockito.verify(authorRepository).findByName("Honoré de Balzac");
    //     Mockito.verify(bookRepository).save(any(Book.class));

    //     assertNotNull(result, "Resulting BookDTO should not be null");
    //     assertEquals(42L, result.getBookId());
    //     assertEquals("Le Père Goriot (Updated)", result.getTitle());
    //     assertEquals("Honoré de Balzac", result.getAuthorName());
    // }

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
    // @Test
    // void testSaveBook_withNewAuthor() {
    //     // GIVEN
    //     BookDto dto = new BookDto();
    //     dto.setBookId(null);
    //     dto.setTitle("New Book Title");
    //     dto.setPublicationYear(2025);
    //     dto.setAuthorName("New Author Name");

    //     // Return empty list => no author found
    //     Iterable<Author> noAuthors = List.of();
    //     Mockito.when(authorRepository.findByName("New Author Name")).thenReturn(noAuthors);

    //     // After creating a new Author, let's say it has ID 99
    //     Author newAuthor = new Author(99L, "New Author Name");
    //     Mockito.when(authorRepository.save(any(Author.class))).thenReturn(newAuthor);

    //     Book savedMock = new Book(100L, newAuthor, "New Book Title", 2025, null, null);

    //     Mockito.when(bookRepository.save(any(Book.class))).thenReturn(savedMock);

    //     // WHEN
    //     BookDto result = bookService.saveBook(dto);

    //     // THEN
    //     Mockito.verify(authorRepository).findByName("New Author Name");
    //     // We expect to save a new author
    //     Mockito.verify(authorRepository).save(any(Author.class));
    //     // We expect to save a new book
    //     Mockito.verify(bookRepository).save(any(Book.class));

    //     assertNotNull(result, "Resulting BookDTO should not be null");
    //     assertEquals(100L, result.getBookId());
    //     assertEquals("New Author Name", result.getAuthorName());
    // }
}
