package fr.ensai.demo.dto;

public class BookDto {

    private Long bookId;
    private String title;
    private int publicationYear;

    private String authorName;
    private String genreLabel;
    private String countryName;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public BookDto() {
    }

    public BookDto(Long bookId, String title, int publicationYear,
            String authorName, String genreLabel, String countryName) {
        this.bookId = bookId;
        this.title = title;
        this.publicationYear = publicationYear;
        this.authorName = authorName;
        this.genreLabel = genreLabel;
        this.countryName = countryName;
    }

    public BookDto(String title, int publicationYear, String authorName, String genreLabel, String countryName) {
        this(null, title, publicationYear, authorName, genreLabel, countryName);
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGenreLabel() {
        return genreLabel;
    }

    public void setGenreLabel(String genreLabel) {
        this.genreLabel = genreLabel;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "BookDto(" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", authorName='" + authorName + '\'' +
                ", genreLabel='" + genreLabel + '\'' +
                ", countryName='" + countryName + '\'' +
                ')';
    }
}
