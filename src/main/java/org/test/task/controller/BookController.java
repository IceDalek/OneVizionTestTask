package org.test.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.test.task.dto.CreateBookDTO;
import org.test.task.entity.Book;
import org.test.task.service.BookService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public void createBook(@RequestBody CreateBookDTO createBookDTO) {
        bookService.createBook(createBookDTO);
    }

    @GetMapping("/byTitle")
    public List<Book> getAllByTitle() {
        return bookService.getAllByTitle();
    }

    @GetMapping("/byAuthor")
    public List<Book> getAllByAuthor(@RequestParam String author) {
        return bookService.getAllByAuthor(author);
    }

    @GetMapping("/byMostFrequentChar")
    public List<Map<String, String>> getAllByMostFrequentChar(@RequestParam Character character) {
        return bookService.getBooksByMostFrequentChar(character);
    }

}
