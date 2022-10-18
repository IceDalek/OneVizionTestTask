package org.test.task.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.task.dao.BookRepository;
import org.test.task.dto.CreateBookDTO;
import org.test.task.entity.Book;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BookService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookRepository bookRepository;

    // В задании указано использовать streamApi, но логически он не подходит ни под один из методов,
    // поэтому просто использую фильтр
    // stream api также есть в тестах
    public List<Book> getAllByTitle() {
        return bookRepository.getAllByTitle().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Book> getAllByAuthor(String author) {
        return bookRepository.getAllByAuthor(author).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void createBook(CreateBookDTO createBookDTO) {
        bookRepository.save(modelMapper.map(createBookDTO, Book.class));
    }

    public List<Map<String, String>> getBooksByMostFrequentChar(Character character) {
        return bookRepository.getBooksByMostFrequentChar(character);
    }


}
