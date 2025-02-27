package fr.ensai.demo.service;

import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.events.BookRemovedFromCollectionEvent;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.BookCollection;
import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.GenreRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.BookCollectionRepository;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

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
    @Autowired
    private BookCollectionRepository bookCollectionRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Iterable<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleIgnoreCase(title);
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        // Get or create related entities
        Author author = getOrCreateAuthor(bookDto.getAuthorName());
        Genre genre = getOrCreateGenre(bookDto.getGenreLabel());
        Country country = getOrCreateCountry(bookDto.getCountryName());

        // Create a new Book (null id for new entity)
        Book book = new Book(null, author, bookDto.getTitle(), bookDto.getPublicationYear(), genre, country);
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    /**
     * Update an existing Book from a BookDto.
     * Throws IllegalArgumentException if the book ID is null or if the book is not found.
     */
    @Transactional
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto.getBookId() == null) {
            throw new IllegalArgumentException("Book id is required for update");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookDto.getBookId());
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        Book book = optionalBook.get();
        // Update basic fields
        book.setTitle(bookDto.getTitle());
        book.setPublicationYear(bookDto.getPublicationYear());

        // Update or create related entities
        Author author = getOrCreateAuthor(bookDto.getAuthorName());
        book.setAuthor(author);

        Genre genre = getOrCreateGenre(bookDto.getGenreLabel());
        book.setGenre(genre);

        Country country = getOrCreateCountry(bookDto.getCountryName());
        book.setCountry(country);

        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }

    @Transactional
    public boolean deleteBook(Long bookId) {
        // 1. Retrieve the book to be deleted.
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            return false;
        }
        Book bookToDelete = bookOpt.get();

        // 2. Find all collections that contain this book (using Iterable).
        Iterable<BookCollection> collections = bookCollectionRepository.findByBooks_BookId(bookId);

        // 3. Publish a removal event for each affected collection.
        for (BookCollection bc : collections) {
            BookCollectionDto collectionDto = BookCollectionService.convertToDto(bc);
            eventPublisher.publishEvent(
                    new BookRemovedFromCollectionEvent(collectionDto, convertToDto(bookToDelete)));
        }

        // 4. Proceed to delete the book.
        bookRepository.delete(bookToDelete);

        return true;
    }

    /**
     * Retrieve all Books and convert them to BookDto.
     */
    public Iterable<BookDto> getBooks() {
        Iterable<Book> books = bookRepository.findAll();
        List<BookDto> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(convertToDto(book));
        }
        return dtos;
    }

    /**
     * Retrieve a Book by its id and convert it to a BookDto.
     * Throws IllegalArgumentException if the book is not found.
     */
    public BookDto getBookById(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        return convertToDto(optionalBook.get());
    }

    // Helper method to get an Author by name or create one if not found.
    private Author getOrCreateAuthor(String name) {
        if (name == null) {
            return null;
        }
        Iterable<Author> authors = authorRepository.findByName(name);
        for (Author a : authors) {
            return a;
        }
        // Not found; create a new Author.
        Author newAuthor = new Author(name);
        return authorRepository.save(newAuthor);
    }

    // Helper method to get a Genre by label or create one if not found.
    private Genre getOrCreateGenre(String label) {
        if (label == null) {
            return null;
        }
        Iterable<Genre> genres = genreRepository.findByLabel(label);
        for (Genre g : genres) {
            return g;
        }
        // Not found; create a new Genre.
        Genre newGenre = new Genre(label);
        return genreRepository.save(newGenre);
    }

    // Helper method to get a Country by name or create one if not found.
    private Country getOrCreateCountry(String name) {
        if (name == null) {
            return null;
        }
        Iterable<Country> countries = countryRepository.findByName(name);
        for (Country c : countries) {
            return c;
        }
        // Not found; create a new Country.
        Country newCountry = new Country(name);
        return countryRepository.save(newCountry);
    }

    // Helper method to convert a Book entity to a BookDto.
    static BookDto convertToDto(Book book) {
        return new BookDto(
                book.getBookId(),
                book.getTitle(),
                book.getPublicationYear(),
                (book.getAuthor() != null) ? book.getAuthor().getName() : null,
                (book.getGenre() != null) ? book.getGenre().getLabel() : null,
                (book.getCountry() != null) ? book.getCountry().getName() : null);
    }

    /**
     * Create or update a Book using a DTO, then return the saved entity as a DTO.
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
    */
    /* Convert a Book entity to a DTO.
    private BookDto entityToDTO(Book book) {Let's try to make it 
        return new BookDto(
                book.getBookId(),
                book.getTitle(),
                book.getPublicationYear(),
                (book.getAuthor() != null) ? book.getAuthor().getName() : null,
                (book.getGenre() != null) ? book.getGenre().getLabel() : null,
                (book.getCountry() != null) ? book.getCountry().getName() : null);
    }
    */
}
