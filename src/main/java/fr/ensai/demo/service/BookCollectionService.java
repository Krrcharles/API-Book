package fr.ensai.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.BookCollection;
import fr.ensai.demo.repository.BookCollectionRepository;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.events.BookAddedToCollectionEvent;
import fr.ensai.demo.events.BookRemovedFromCollectionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookCollectionService {

    @Autowired
    private BookCollectionRepository bookCollectionRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private BookService bookService;

    @Transactional
    public BookCollectionDto createBookCollection(BookCollectionDto bookCollectionDto) {
        BookCollection collection = new BookCollection(
                bookCollectionDto.getName(),
                bookCollectionDto.getDistanceJaro(),
                bookCollectionDto.getDistanceJaccard());
        if (bookCollectionDto.getBookIds() != null) {
            for (Long bookId : bookCollectionDto.getBookIds()) {
                Optional<Book> bookOpt = bookRepository.findById(bookId);
                if (bookOpt.isEmpty()) {
                    throw new IllegalArgumentException("Book with id " + bookId + " not found");
                }
                collection.addBook(bookOpt.get());
            }
        }
        BookCollection saved = bookCollectionRepository.save(collection);
        return convertToDto(saved);
    }

    @Transactional
    public BookCollectionDto copyBookCollection(Long collectionId, String newName) {
        Optional<BookCollection> opt = bookCollectionRepository.findById(collectionId);
        if (opt.isEmpty()) {
            return null;
        }
        BookCollection original = opt.get();
        BookCollection copy = new BookCollection(newName, original.getDistanceJaro(), original.getDistanceJaccard());
        for (Book book : original.getBooks()) {
            copy.addBook(book);
        }
        BookCollection saved = bookCollectionRepository.save(copy);
        return convertToDto(saved);
    }

    public Iterable<BookCollectionDto> getBookCollections() {
        List<BookCollectionDto> dtos = new ArrayList<>();
        for (BookCollection collection : bookCollectionRepository.findAll()) {
            dtos.add(convertToDto(collection));
        }
        return dtos;
    }

    public Optional<BookCollectionDto> getBookCollection(Long collectionId) {
        Optional<BookCollection> opt = bookCollectionRepository.findById(collectionId);
        return opt.map(BookCollectionService::convertToDto);
    }

    /**
     * Ajoute un nouveau livre à la collection à partir d'un BookDto.
     * Cette méthode délègue la création du livre à BookService.createBook.
     */
    @Transactional
    public BookCollectionDto addBookToCollection(Long collectionId, BookDto bookDto) {
        // Create the new book via BookService
        BookDto createdBook = bookService.createBook(bookDto);
        // Delegate to the method that adds an existing book by its id
        BookCollectionDto updatedCollection = addBookToCollection(collectionId, createdBook.getBookId());
        // Publish a change event indicating a book was added
        eventPublisher.publishEvent(
                new BookAddedToCollectionEvent(updatedCollection, createdBook));
        return updatedCollection;
    }

    /**
     * Ajoute un livre existant (via son identifiant) à la collection.
     */
    @Transactional
    public BookCollectionDto addBookToCollection(Long collectionId, Long bookId) {
        Optional<BookCollection> optCollection = bookCollectionRepository.findById(collectionId);
        Optional<Book> optBook = bookRepository.findById(bookId);
        if (optCollection.isEmpty() || optBook.isEmpty()) {
            return null;
        }
        BookCollection collection = optCollection.get();
        Book book = optBook.get();
        collection.addBook(book);
        bookCollectionRepository.save(collection);
        BookCollectionDto updatedCollection = convertToDto(collection);
        // Publish a change event with the added book
        eventPublisher.publishEvent(
                new BookAddedToCollectionEvent(updatedCollection, BookService.convertToDto(book)));
        return updatedCollection;
    }

    @Transactional
    public BookCollectionDto removeBookFromCollection(Long collectionId, Long bookId) {
        Optional<BookCollection> optCollection = bookCollectionRepository.findById(collectionId);
        Optional<Book> optBook = bookRepository.findById(bookId);
        if (optCollection.isEmpty() || optBook.isEmpty()) {
            return null;
        }
        BookCollection collection = optCollection.get();
        Book book = optBook.get();
        collection.removeBook(book);
        bookCollectionRepository.save(collection);
        BookCollectionDto updatedCollection = convertToDto(collection);
        // Publish a change event with the removed book
        eventPublisher.publishEvent(
                new BookRemovedFromCollectionEvent(updatedCollection, BookService.convertToDto(book)));
        return updatedCollection;
    }

    public Iterable<BookDto> getBooks(Long collectionId, String orderBy) {
        Optional<BookCollection> opt = bookCollectionRepository.findById(collectionId);
        if (opt.isEmpty()) {
            return new ArrayList<>();
        }
        List<Book> books = new ArrayList<>(opt.get().getBooks());
        if (orderBy != null) {
            switch (orderBy.toLowerCase()) {
                case "title":
                    books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "publicationyear":
                    books.sort(Comparator.comparingInt(Book::getPublicationYear));
                    break;
                case "author":
                    books.sort(Comparator.comparing(b -> b.getAuthor().getName(), String.CASE_INSENSITIVE_ORDER));
                    break;
                default:
                    books.sort(Comparator.comparing(Book::getBookId));
                    break;
            }
        }
        return books.stream()
                .map(book -> new BookDto(
                        book.getBookId(),
                        book.getTitle(),
                        book.getPublicationYear(),
                        (book.getAuthor() != null) ? book.getAuthor().getName() : null,
                        (book.getGenre() != null) ? book.getGenre().getLabel() : null,
                        (book.getCountry() != null) ? book.getCountry().getName() : null))
                .collect(Collectors.toList());
    }

    // NOTE: pas très élaboré
    public Double compareCollections(Long collectionId1, Long collectionId2, String metric) {
        Optional<BookCollection> opt1 = bookCollectionRepository.findById(collectionId1);
        Optional<BookCollection> opt2 = bookCollectionRepository.findById(collectionId2);
        if (opt1.isEmpty() || opt2.isEmpty()) {
            return null;
        }
        BookCollection c1 = opt1.get();
        BookCollection c2 = opt2.get();
        double v1;
        double v2;
        switch (metric) {
            case "size":
                v1 = c1.getBooks().size();
                v2 = c2.getBooks().size();
                break;
            case "jaro":
                v1 = c1.getDistanceJaro();
                v2 = c2.getDistanceJaro();
                break;
            case "jaccard":
                v1 = c1.getDistanceJaccard();
                v2 = c2.getDistanceJaccard();
                break;
            default:
                throw new IllegalArgumentException("Unknown method: " + metric);
        }
        return Math.abs(v1 - v2);
    }

    static BookCollectionDto convertToDto(BookCollection collection) {
        List<Long> bookIds = collection.getBooks().stream()
                .map(Book::getBookId)
                .collect(Collectors.toList());
        return new BookCollectionDto(
                collection.getCollectionId(),
                collection.getName(),
                collection.getDistanceJaro(),
                collection.getDistanceJaccard(),
                bookIds);
    }
}