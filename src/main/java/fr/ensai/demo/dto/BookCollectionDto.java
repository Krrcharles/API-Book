package fr.ensai.demo.dto;

import java.util.List;

public class BookCollectionDto {

    private Long collectionId;
    private String name;
    private Double distanceJaro;
    private Double distanceJaccard;
    private List<BookDto> books;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public BookCollectionDto() {}

    public BookCollectionDto(Long collectionId, String name, Double distanceJaro, Double distanceJaccard, List<BookDto> books) {
        this.collectionId = collectionId;
        this.name = name;
        this.distanceJaro = distanceJaro;
        this.distanceJaccard = distanceJaccard;
        this.books = books;
    }

    public BookCollectionDto(String name, Double distanceJaro, Double distanceJaccard, List<BookDto> books) {
        this(null, name, distanceJaro, distanceJaccard, books);
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

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }
}
