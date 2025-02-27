package fr.ensai.demo.service;

import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.GenreRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import java.util.stream.StreamSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

@Service
public class CsvImportService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Transactional
    public void importBooksFromCsv(File csvFile, String charSetName) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(csvFile), Charset.forName(charSetName))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setDelimiter(',')
                    .setQuote('"')
                    .setTrim(true)
                    .build();

            Iterable<CSVRecord> records = csvFormat.parse(reader);

            for (CSVRecord record : records) {
                // Map CSV columns to variables:
                // 0: title, 1: author, 2: year, 3: genre, 4: country
                String title = record.get(0);
                String authorName = record.get(1);
                int year = Integer.parseInt(record.get(2));
                String genreLabel = record.get(3);
                String countryName = record.get(4);

                // Find or create the Author
                Author author = StreamSupport.stream(authorRepository.findByName(authorName).spliterator(), false)
                        .findFirst()
                        .orElseGet(() -> authorRepository.save(new Author(authorName)));

                // Find or create the Genre
                Genre genre = StreamSupport.stream(genreRepository.findByLabel(genreLabel).spliterator(), false)
                        .findFirst()
                        .orElseGet(() -> genreRepository.save(new Genre(genreLabel)));

                // Find or create the Country
                Country country = StreamSupport.stream(countryRepository.findByName(countryName).spliterator(), false)
                        .findFirst()
                        .orElseGet(() -> countryRepository.save(new Country(countryName)));

                // Create and save the Book
                Book book = new Book(author, title, year, genre, country);
                bookRepository.save(book);
            }
        }
    }
}
