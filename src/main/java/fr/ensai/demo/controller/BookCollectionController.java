package fr.ensai.demo.controller;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.service.BookCollectionService;
import fr.ensai.demo.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book_collection")
public class BookCollectionController {
    @Autowired
    private BookCollectionService bookCollectionService;

/*     @PutMapping
    public BookCollectionDto updateBook(@RequestBody BookCollectionDto bookCollectionDto) {
        return bookCollectionService.saveBookCollection(bookCollectionDto);
    } */
}
