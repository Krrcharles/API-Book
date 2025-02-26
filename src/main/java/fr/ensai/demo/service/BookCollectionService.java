package fr.ensai.demo.service;

import java.util.List;
import java.util.Optional;
import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.GenreRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.BookCollectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookCollectionService {
    @Autowired
    private BookCollectionRepository bookCollectionRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CountryRepository countryRepository;

    public BookCollectionDto createBookCollection(BookCollectionDto bookCollectionDto) {
        return null;
    }

    public BookCollectionDto copyBookCollection(Long collectionId, String newName) {
        return null;
    }

    public Iterable<BookCollectionDto> getBookCollections() {
        return null;
    }

    public Optional<BookCollectionDto> getBookCollection(Long collectionId) {
        return null;
    }

    public BookCollectionDto addBookToCollection(Long collectionId, BookDto bookDto) {
        return null;
    }

    public BookCollectionDto addBookToCollection(Long collectionId, Long bookId) {
        return null;
    }

    public BookCollectionDto removeBookFromCollection(Long collectionId, Long bookId) {
        return null;
    }

    public Iterable<BookDto> getBooks(Long collectionId, String orderBy) {
        return null;
    }

    public Double compareCollections(Long collectionId1, Long collectionId2, String method) {
        return null;
    }

    /*     
    public BookCollection createCollection(String name, List<Long> bookIds) {
        /// Vérifier que tous les livres existent
        for (Long bookId : bookIds) {
            if (!bookRepository.findById(bookId).isPresent()) {
                throw new IllegalArgumentException("Le livre avec l'ID " + bookId + " n'existe pas");
            }
        }
    
        BookCollection bookCollection = new BookCollection(1L, name, 0d, 0d);
        List<Book> books = (List<Book>) bookRepository.findAllById(bookIds);
        bookCollection.setBooks(books);
        BookCollection savedBookCollection = bookCollectionRepository.save(bookCollection);
        return savedBookCollection;
    }
    
    public BookCollection saveBookCollection(BookCollection bookCollection) {
        BookCollection savedBookCollection = bookCollectionRepository.save(bookCollection);
        return savedBookCollection;
    }
    
    public BookCollection duplicateCollection(Long originalCollectionId, String newName) {
        BookCollection originalCollection = bookCollectionRepository.findById(originalCollectionId).orElseThrow();
        BookCollection newCollection = new BookCollection(newName, 0d, 0d);
        newCollection.setBooks(originalCollection.getBooks());
        return bookCollectionRepository.save(newCollection);
    } */
}
