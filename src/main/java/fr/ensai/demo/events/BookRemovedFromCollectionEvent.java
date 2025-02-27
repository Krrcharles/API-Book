package fr.ensai.demo.events;

import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.dto.BookDto;

public class BookRemovedFromCollectionEvent {
    private final BookCollectionDto collection;
    private final BookDto book;

    public BookRemovedFromCollectionEvent(BookCollectionDto collection, BookDto book) {
        this.collection = collection;
        this.book = book;
    }

    public BookCollectionDto getCollection() {
        return collection;
    }

    public BookDto getBook() {
        return book;
    }
}
