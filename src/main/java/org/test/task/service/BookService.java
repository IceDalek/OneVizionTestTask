package org.test.task.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.test.task.dao.BookRepository;
import org.test.task.dto.CreateBookDTO;
import org.test.task.entity.Book;

import java.util.List;
import java.util.Map;


@Service
public class BookService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookRepository bookRepository;


    public List<Book> getAllByTitle() {
        return bookRepository.getAllByTitle();
    }

    public Map<String, List<Book>> groupByAuthor() {
        return bookRepository.groupByAuthor();
    }

    public Book createBook(CreateBookDTO createBookDTO) {
        return bookRepository.save(modelMapper.map(createBookDTO, Book.class));
    }

    public List<Map<String, String>> getBooksByMostFrequentChar(Character character) {
        return bookRepository.getBooksByMostFrequentChar(character);
    }


}
