package fr.ensai.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "t_author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    @NotEmpty(message = "Name (author) missing")
    @Column(nullable = false)
    private String name;

    /**
     * One author can have many books.
     * The "mappedBy = 'author'" must match the 'author' field name
     * in the Book entity (@ManyToOne side).
     * TODO : Trust.
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Book> books = new ArrayList<>();

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    protected Author() {
        // Required by JPA
    }

    public Author(Long authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    // Convenience constructor if you don't want to specify authorId
    public Author(String name) {
        this(null, name);
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }

    @Override
    public String toString() {
        return "Author(authorId=" + authorId + ", name=" + name + ")";
    }
}
