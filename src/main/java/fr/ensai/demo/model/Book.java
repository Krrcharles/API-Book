package fr.ensai.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@Entity
@Table(name = "t_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") 
    private Long bookId;

    @NotEmpty(message = "Title (book) missing")
    @Column(nullable = false)
    private String title;

    @Column(name="publication_year", nullable = false)
    private int publicationYear;

    /**
     * Many books can be by one author.
     * The foreign key column is 'author_id' in t_book.
     */
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    /**
     * Many books can have one genre.
     * The foreign key column is 'genre_id' in t_book.
     */
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    /**
     * Many books can be associated with one country.
     * The foreign key column is 'country_id' in t_book.
     */
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------
    
    protected Book() {
        // Required by JPA
    }

    public Book(Long bookId, Author author, String title, int publicationYear, Genre genre, Country country) {
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.country = country;
    }

    public Book(Author author, String title, int publicationYear, Genre genre, Country country) {
        this(null, author, title, publicationYear, genre, country);
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        this.country = country;
    }

    // Equals et HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear &&
               Objects.equals(author, book.author) &&
               Objects.equals(title, book.title) &&
               Objects.equals(genre, book.genre) &&
               Objects.equals(country, book.country);
    }
}
