package fr.ensai.demo.controller;

import java.util.Optional;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.service.BookCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2 Book Collections", description = "Book Collection operations")
@RestController
@RequestMapping("/bookCollection")
public class BookCollectionController {
    @Autowired
    private BookCollectionService bookCollectionService;

    @PostMapping
    public ResponseEntity<BookCollectionDto> createBookCollection(@RequestBody BookCollectionDto bookCollectionDto) {
        BookCollectionDto created = bookCollectionService.createBookCollection(bookCollectionDto);
        if (created == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book Collection creation failed");
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/copy/{collectionId}/{newName}")
    public ResponseEntity<BookCollectionDto> copyBookCollection(@PathVariable Long collectionId,
            @PathVariable String newName) {
        BookCollectionDto copied = bookCollectionService.copyBookCollection(collectionId, newName);
        if (copied == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Collection to copy not found");
        }
        return new ResponseEntity<>(copied, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<BookCollectionDto>> getBookCollections() {
        Iterable<BookCollectionDto> collections = bookCollectionService.getBookCollections();
        if (!collections.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book collections found");
        }
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<Optional<BookCollectionDto>> getBookCollection(@PathVariable Long collectionId) {
        Optional<BookCollectionDto> result = bookCollectionService.getBookCollection(collectionId);
        if (!result.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Collection not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("{collectionId}/book/")
    public ResponseEntity<BookCollectionDto> addBookToCollection(@PathVariable Long collectionId,
            @RequestBody BookDto bookDto) {
        BookCollectionDto updated = bookCollectionService.addBookToCollection(collectionId, bookDto);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Collection not found for adding book");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("{collectionId}/book/{bookId}")
    public ResponseEntity<BookCollectionDto> addBookToCollection(@PathVariable Long collectionId,
            @PathVariable Long bookId) {
        BookCollectionDto updated = bookCollectionService.addBookToCollection(collectionId, bookId);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book Collection or Book not found for adding book");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("{collectionId}/book/{bookId}")
    public ResponseEntity<BookCollectionDto> removeBookFromCollection(@PathVariable Long collectionId,
            @PathVariable Long bookId) {
        BookCollectionDto updated = bookCollectionService.removeBookFromCollection(collectionId, bookId);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book Collection or Book not found for removing book");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("{collectionId}/books")
    public ResponseEntity<Iterable<BookDto>> getBooks(
            @PathVariable Long collectionId,
            @RequestParam("order") String orderBy) {
        Iterable<BookDto> books = bookCollectionService.getBooks(collectionId, orderBy);
        if (!books.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No books found in collection " + collectionId + " for order: " + orderBy);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/compare/{collectionId1}/{collectionId2}/{metric}")
    public ResponseEntity<Double> compareCollections(@PathVariable Long collectionId1, @PathVariable Long collectionId2,
            @PathVariable String metric) {
        if (metric == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comparison metric must be provided");
        }
        metric = metric.toLowerCase();
        if (!(metric.equals("size") || metric.equals("jaro") || metric.equals("jaccard"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Comparison metric must be either 'size' or 'jaro' or 'jaccard'");
        }
        Double result = bookCollectionService.compareCollections(collectionId1, collectionId2, metric);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Comparison failed: collections not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
