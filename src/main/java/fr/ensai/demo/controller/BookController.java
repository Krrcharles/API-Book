package fr.ensai.demo.controller;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "1 Books", description = "Books CRUD operations")
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto created = bookService.createBook(bookDto);
        if (created == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book creation failed");
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
        BookDto updated = bookService.updateBook(bookDto);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found for update");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        boolean found = bookService.deleteBook(bookId);
        if (!found) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found for deletion");
        }
        return new ResponseEntity<>("Book " + bookId + " deleted", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<BookDto>> getBooks() {
        Iterable<BookDto> books = bookService.getBooks();
        if (!books.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found");
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        BookDto book = bookService.getBookById(bookId);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
