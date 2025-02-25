package fr.ensai.demo.service;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.GenreRepository;
import fr.ensai.demo.repository.CountryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CountryRepository countryRepository;

    public Iterable<Book> findBooksByTitle(String title) {
      return bookRepository.findByTitleIgnoreCase(title);
    }
    
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Create or update a Book using a DTO, then return the saved entity as a DTO.
     * TODO : Trust...
     */
    @Transactional
    public BookDto saveBook(BookDto dto) {
        // If no ID => new book, else load existing or create new if not found
        Book nullBook = new Book(null, null, 0, null, null);
        Book book = (dto.getBookId() == null)
                ? nullBook
                : bookRepository.findById(dto.getBookId()).orElse(nullBook);

        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());

        // 1) Find authors by name
        Author foundAuthor = null;
        if (dto.getAuthorName() != null) {
            Iterable<Author> authors = authorRepository.findByName(dto.getAuthorName());
            // Attempt to get the first one from the iterable
            for (Author a : authors) {
                foundAuthor = a;
                break;
            }

            // 2) If no matching author, create a new one
            if (foundAuthor == null) {
                Author newAuthor = new Author(dto.getAuthorName());
                foundAuthor = authorRepository.save(newAuthor);
            }
        }

        book.setAuthor(foundAuthor);

        // Gestion du Genre
        Genre foundGenre = null;
        if (dto.getGenreLabel() != null) {
            Iterable<Genre> genres = genreRepository.findByLabel(dto.getGenreLabel());
            for (Genre g : genres) {
                foundGenre = g;
                break;
            }

            if (foundGenre == null) {
                Genre newGenre = new Genre(dto.getGenreLabel());
                foundGenre = genreRepository.save(newGenre);
            }
        }
        book.setGenre(foundGenre);

        // Gestion du Country
        Country foundCountry = null;
        if (dto.getCountryName() != null) {
            Iterable<Country> countries = countryRepository.findByName(dto.getCountryName());
            for (Country c : countries) {
                foundCountry = c;
                break;
            }

            if (foundCountry == null) {
                Country newCountry = new Country(dto.getCountryName());
                foundCountry = countryRepository.save(newCountry);
            }
        }
        book.setCountry(foundCountry);

        Book saved = bookRepository.save(book);

        return entityToDTO(saved);
    }

    /**
     * Convert a Book entity to a DTO.
     * TODO : Trust...
     */
    private BookDto entityToDTO(Book book) {
        return new BookDto(
            book.getBookId(),
            book.getTitle(),
            book.getPublicationYear(),
            (book.getAuthor()  != null) ? book.getAuthor().getName()  : null,
            (book.getGenre()   != null) ? book.getGenre().getLabel()  : null,
            (book.getCountry() != null) ? book.getCountry().getName() : null
        );
    }
}
