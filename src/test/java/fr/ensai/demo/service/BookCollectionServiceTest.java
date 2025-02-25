package fr.ensai.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.ensai.demo.model.Book;
import fr.ensai.demo.model.BookCollection;
import fr.ensai.demo.model.Author;
import fr.ensai.demo.model.Genre;
import fr.ensai.demo.model.Country;
import fr.ensai.demo.repository.BookCollectionRepository;
import fr.ensai.demo.repository.BookRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.test.context.TestPropertySource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class BookCollectionServiceTest {
    Logger LOG = LoggerFactory.getLogger(BookCollectionServiceTest.class);

    @Autowired
    private BookCollectionService bookCollectionService;

    @MockitoBean
    private BookCollectionRepository bookCollectionRepository;
    
    @MockitoBean
    private BookRepository bookRepository;

    @Test
    void createCollection_ShouldSaveCollection() {
        // Arrange
        String collectionName = "Ma Collection";
        List<Long> bookIds = Arrays.asList(1L, 2L);
        Author author1 = new Author(1L, "Victor Hugo");
        Author author2 = new Author(2L, "Albert Camus");
        Genre genre = new Genre(1L, "Roman");
        Country country = new Country(1L, "France");
        Book book1 = new Book(1L, author1, "Les Misérables", 1862, genre, country);
        Book book2 = new Book(2L, author2, "L'Étranger", 1942,  genre, country);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book2));
        
        BookCollection expectedCollection = new BookCollection(1L, collectionName, 0d, 0d);
        expectedCollection.setBooks(Arrays.asList(book1, book2));
        when(bookCollectionRepository.save(any(BookCollection.class))).thenReturn(expectedCollection);

        // Act
        BookCollection result = bookCollectionService.createCollection(collectionName, bookIds);

        // Assert
        assertNotNull(result);
        assertEquals(collectionName, result.getName());
        assertEquals(bookIds, result.getBookIds());
        verify(bookCollectionRepository).save(any(BookCollection.class));
    }

    @Test
    void createCollection_WithInvalidBookId_ShouldThrowException() {
        // Arrange
        String collectionName = "Ma Collection";
        List<Long> bookIds = Arrays.asList(1L, 999L);
        Author author = new Author(1L, "Victor Hugo");
        Genre genre = new Genre(1L, "Roman");
        Country country = new Country(1L, "France");
        Book book1 = new Book(author, "Les Misérables", 1862, genre, country);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> bookCollectionService.createCollection(collectionName, bookIds));
    }

    // @Test
    void duplicateCollection_ShouldDuplicateCollectionWithNewNameAndBooks() {
        // Arrange
        Long originalCollectionId = 1L;
        String originalName = "Collection Originale";
        
        // Création des objets Book
        Author author1 = new Author(1L, "Victor Hugo");
        Genre genre = new Genre(1L, "Roman");
        Country country = new Country(1L, "France");
        Book book1 = new Book(1L, author1, "Les Misérables", 1862, genre, country);
        
        Author author2 = new Author(2L, "Albert Camus");
        Book book2 = new Book(2L, author2, "L'Étranger", 1942, genre, country);
        
        List<Book> books = Arrays.asList(book1, book2);
        
        // Création de la collection d'origine avec une liste de Books
        BookCollection originalCollection = new BookCollection(originalCollectionId, originalName, 0d, 0d);
        originalCollection.setBooks(books);
        
        // Simulation de la récupération de la collection d'origine
        when(bookCollectionRepository.findById(originalCollectionId))
                .thenReturn(Optional.of(originalCollection));
        
        // Nouveau nom pour la collection dupliquée
        String newName = "Collection Dupliquée";
        
        // On simule le comportement de sauvegarde qui retourne la nouvelle collection avec un nouvel ID et la même liste de Books
        BookCollection expectedDuplicatedCollection = new BookCollection(2L, newName, 0d, 0d);
        expectedDuplicatedCollection.setBooks(books);
        when(bookCollectionRepository.save(any(BookCollection.class)))
                .thenReturn(expectedDuplicatedCollection);
        
        // Act
        BookCollection duplicatedCollection = bookCollectionService.duplicateCollection(originalCollectionId, newName);
        
        // Assert
        assertNotNull(duplicatedCollection);
        assertEquals(newName, duplicatedCollection.getName());
        assertEquals(books, duplicatedCollection.getBooks());
        verify(bookCollectionRepository).save(any(BookCollection.class));
    }
    
}
