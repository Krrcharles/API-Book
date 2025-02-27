package fr.ensai.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @NotEmpty(message = "Label (genre) missing")
    @Column(nullable = false)
    private String label;

    /**
     * One genre can be linked to many books.
     * "mappedBy = 'genre'" must match the field name in Book.java
     * where @ManyToOne is declared.
     */
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Book> books = new ArrayList<>();

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    protected Genre() {
        // Required by JPA
    }

    public Genre(Long genreId, String label) {
        this.genreId = genreId;
        this.label = label;
    }

    public Genre(String label) {
        this(null, label);
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setGenre(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setGenre(null);
    }

    @Override
    public String toString() {
        return "Genre(" +
                "genreId=" + genreId +
                ", label=\"" + label + "\"" +
                ")";
    }
}
