package fr.ensai.demo.service;

import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.BookCollection;
import fr.ensai.demo.repository.AuthorRepository;
import fr.ensai.demo.repository.BookCollectionRepository;
import fr.ensai.demo.repository.BookRepository;
import fr.ensai.demo.repository.CountryRepository;
import fr.ensai.demo.repository.GenreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BookCollection createCollection(String name, List<Long> bookIds) {
        // Vérifier que tous les livres existent
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
    }
}
