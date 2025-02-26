package fr.ensai.demo.service;

import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.GenreRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class CsvImportServiceTest {

    @Autowired
    private CsvImportService csvImportService;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private CountryRepository countryRepository;

    private File tempCsvFile;

    @BeforeEach
    public void setup() throws IOException {
        // Create a temporary CSV file that will be used by each test.
        tempCsvFile = File.createTempFile("test_books", ".csv");
    }

    @AfterEach
    public void cleanup() {
        if (tempCsvFile != null && tempCsvFile.exists()) {
            tempCsvFile.delete();
        }
    }

    @Test
    public void testImportBooksFromCsv_withNewEntities() throws IOException {
        // Write CSV content for a single book record.
        // CSV columns: title, author, year, genre, country
        String charsetName = "UTF-8";
        String csvContent = "Les poèmes de... ,Paul Celan ,1952,Poésie (œuvre poétique) ,Roumanie\n";
        try (FileWriter writer = new FileWriter(tempCsvFile, Charset.forName(charsetName))) {
            writer.write(csvContent);
        }

        // Simulate that the repositories do not find existing entities.
        when(authorRepository.findByName("Paul Celan")).thenReturn(Collections.emptyList());
        when(genreRepository.findByLabel("Poésie (œuvre poétique)")).thenReturn(Collections.emptyList());
        when(countryRepository.findByName("Roumanie")).thenReturn(Collections.emptyList());

        // Simulate saving new entities.
        Author savedAuthor = new Author(3L, "Paul Celan");
        Genre savedGenre = new Genre(1L, "Poésie (œuvre poétique)");
        Country savedCountry = new Country(1L, "Roumanie");
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        when(genreRepository.save(any(Genre.class))).thenReturn(savedGenre);
        when(countryRepository.save(any(Country.class))).thenReturn(savedCountry);

        // Simulate saving the Book.
        Book savedBook = new Book(42L, savedAuthor, "Paul Celan", 1835, savedGenre, savedCountry);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act: call the service to import the CSV.
        csvImportService.importBooksFromCsv(tempCsvFile, charsetName);

        // Assert: verify that the repositories are queried and the save methods are called.
        verify(authorRepository).findByName("Paul Celan");
        verify(authorRepository).save(any(Author.class));
        verify(genreRepository).findByLabel("Poésie (œuvre poétique)");
        verify(genreRepository).save(any(Genre.class));
        verify(countryRepository).findByName("Roumanie");
        verify(countryRepository).save(any(Country.class));
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void testImportBooksFromCsv_withExistingEntities() throws IOException {
        // Write CSV content for a single book record.
        String charsetName = "UTF-8";
        String csvContent = "L'Étranger,Albert Camus,1942,Roman,France\n";
        try (FileWriter writer = new FileWriter(tempCsvFile)) {
            writer.write(csvContent);
        }

        // Simulate existing entities for this record.
        Author existingAuthor = new Author(2L, "Albert Camus");
        Genre existingGenre = new Genre(1L, "Roman");
        Country existingCountry = new Country(1L, "France");

        when(authorRepository.findByName("Albert Camus")).thenReturn(List.of(existingAuthor));
        when(genreRepository.findByLabel("Roman")).thenReturn(List.of(existingGenre));
        when(countryRepository.findByName("France")).thenReturn(List.of(existingCountry));

        // Simulate saving the book only.
        Book savedBook = new Book(100L, existingAuthor, "L'Étranger", 1942, existingGenre, existingCountry);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act: call the service to import the CSV.
        csvImportService.importBooksFromCsv(tempCsvFile, charsetName);

        // Assert: verify that the find methods are called and no new entity is saved.
        verify(authorRepository).findByName("Albert Camus");
        verify(authorRepository, never()).save(any(Author.class));
        verify(genreRepository).findByLabel("Roman");
        verify(genreRepository, never()).save(any(Genre.class));
        verify(countryRepository).findByName("France");
        verify(countryRepository, never()).save(any(Country.class));
        verify(bookRepository).save(any(Book.class));
    }
}
