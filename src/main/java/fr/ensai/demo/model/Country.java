package fr.ensai.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long countryId;

    @NotEmpty(message = "Name (country) missing")
    @Column(nullable = false)
    private String name;

    /**
     * One country can be linked to many books.
     * "mappedBy = 'country'" must match the field name in Book.java
     * where @ManyToOne is declared.
     */
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Book> books = new ArrayList<>();

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    protected Country() {
        // Required by JPA
    }

    public Country(Long countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }

    public Country(String name) {
        this(null, name);
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
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
        book.setCountry(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setCountry(null);
    }

    @Override
    public String toString() {
        return "Country(" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                ')';
    }
}
