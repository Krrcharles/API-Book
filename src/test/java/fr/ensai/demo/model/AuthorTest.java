package fr.ensai.demo.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class AuthorTest {

    /**
     * DummyBook is a simple subclass of Book for testing purposes.
     * It implements the setAuthor and getAuthor methods so that we can verify
     * the behavior of addBook and removeBook in the Author class.
     */
    private static class DummyBook extends Book {
        private Author author;

        public DummyBook() {
            super();
        }

        @Override
        public void setAuthor(Author author) {
            this.author = author;
        }

        @Override
        public Author getAuthor() {
            return author;
        }
    }

    @Test
    void testDefaultConstructor() {
        Author author = new Author();
        assertNull(author.getAuthorId(), "Default constructor should leave authorId as null");
        assertNull(author.getName(), "Default constructor should leave name as null");
        assertNotNull(author.getBooks(), "Books list should be initialized");
        assertTrue(author.getBooks().isEmpty(), "Books list should be empty");
    }

    @Test
    void testConstructorWithId() {
        Author author = new Author(1L, "Alice");
        assertEquals(1L, author.getAuthorId(), "AuthorId should be set by the constructor");
        assertEquals("Alice", author.getName(), "Name should be set by the constructor");
    }

    @Test
    void testConstructorWithoutId() {
        Author author = new Author("Bob");
        assertNull(author.getAuthorId(), "Convenience constructor should leave authorId as null");
        assertEquals("Bob", author.getName(), "Name should be set by the constructor");
    }

    @Test
    void testSetAndGetAuthorId() {
        Author author = new Author("Test");
        author.setAuthorId(10L);
        assertEquals(10L, author.getAuthorId(), "setAuthorId/getAuthorId should work correctly");
    }

    @Test
    void testSetAndGetName() {
        Author author = new Author("Test");
        author.setName("Charlie");
        assertEquals("Charlie", author.getName(), "setName/getName should work correctly");
    }

    @Test
    void testSetAndGetBooks() {
        Author author = new Author("Test Author");
        List<Book> books = new ArrayList<>();
        DummyBook book1 = new DummyBook();
        DummyBook book2 = new DummyBook();
        books.add(book1);
        books.add(book2);
        author.setBooks(books);
        assertEquals(2, author.getBooks().size(), "Books list should contain two books");
        assertSame(book1, author.getBooks().get(0), "First book should match");
        assertSame(book2, author.getBooks().get(1), "Second book should match");
    }

    @Test
    void testAddBook() {
        Author author = new Author("John Doe");
        DummyBook book = new DummyBook();
        author.addBook(book);
        assertEquals(1, author.getBooks().size(), "Books list should have one book after addBook");
        assertSame(author, book.getAuthor(), "Book's author should be set when added");
    }

    @Test
    void testRemoveBook() {
        Author author = new Author("John Doe");
        DummyBook book = new DummyBook();
        author.addBook(book);
        author.removeBook(book);
        assertTrue(author.getBooks().isEmpty(), "Books list should be empty after removeBook");
        assertNull(book.getAuthor(), "Book's author should be null after removal");
    }
}
