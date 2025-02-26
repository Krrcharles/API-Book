package fr.ensai.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_book_collection")
public class BookCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long collectionId;

    @Column(nullable = false)
    private String name;

    // Matches the columns in the table: distance_jaro and distance_jaccard
    @Column(name = "distance_jaro")
    private Double distanceJaro;

    @Column(name = "distance_jaccard")
    private Double distanceJaccard;

    /**
     * Many collections can contain many books, and vice versa.
     * We define the join table "t_book_collection_book",
     * with matching "collection_id" and "book_id" columns.
     */
    @ManyToMany
    @JoinTable(name = "t_book_collection_book", joinColumns = @JoinColumn(name = "collection_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books = new ArrayList<>();

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    protected BookCollection() {
        // Default constructor for JPA
    }

    public BookCollection(Long collectionId, String name, Double distanceJaro, Double distanceJaccard) {
        this.collectionId = collectionId;
        this.name = name;
        this.distanceJaro = distanceJaro;
        this.distanceJaccard = distanceJaccard;
    }

    public BookCollection(String name, Double distanceJaro, Double distanceJaccard) {
        this(null, name, distanceJaro, distanceJaccard);
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistanceJaro() {
        return distanceJaro;
    }

    public void setDistanceJaro(Double distanceJaro) {
        this.distanceJaro = distanceJaro;
    }

    public Double getDistanceJaccard() {
        return distanceJaccard;
    }

    public void setDistanceJaccard(Double distanceJaccard) {
        this.distanceJaccard = distanceJaccard;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Long> getBookIds() {
        List<Long> bookIds = new ArrayList<>();
        for (Book book : books) {
            bookIds.add(book.getBookId());
        }
        return bookIds;
    }

    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public String toString() {
        return "BookCollection(" +
                "collectionId=" + collectionId +
                ", name='" + name + '\'' +
                ", distanceJaro=" + distanceJaro +
                ", distanceJaccard=" + distanceJaccard +
                ", books=" + books +
                ')';
    }
}
