package fr.ensai.demo.service;

import fr.ensai.demo.model.Book;

public class HelloWorld {
    private String value = "Hello World";

    public String toString() {
        return value;
    }

    public void dummi(Book book) {
        double id = book.getBookId();
    }
}
