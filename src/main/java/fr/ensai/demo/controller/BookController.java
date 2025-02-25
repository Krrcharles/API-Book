package fr.ensai.demo.controller;

import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PutMapping
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookService.saveBook(bookDto);
    }
}
