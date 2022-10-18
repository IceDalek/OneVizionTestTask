package org.test.task.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.test.task.entity.Book;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookRepository {

    private final RowMapper<Book> ROW_MAPPER = (ResultSet resultSet, int rowNum)
            -> new Book(resultSet.getInt("id"),
            resultSet.getString("title"),
            resultSet.getString("author"),
            resultSet.getString("description"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Book> getAllByTitle() {
        return jdbcTemplate.query("select * from book ORDER BY title DESC", ROW_MAPPER);
    }

    public List<Book> getAllByAuthor(String author) {
        return jdbcTemplate.query("select * from book where author = ? ", ROW_MAPPER, author);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(id, title,author,description) VALUES(default, ?, ?, ?)", book.getTitle(), book.getAuthor(), book.getDescription());
    }

    public List<Map<String, String>> getBooksByMostFrequentChar(Character character) {
        String sql = "select author, sum(c) as sum from " +
                "(select author, (select  count(*)  FROM regexp_matches(lower(book.title), ?, 'g')) as c" +
                " from book group by author, title) as acS where c > 0 group by author order by sum DESC limit 10;";
        return jdbcTemplate.query(sql, (ResultSet resultSet, int rowNum)
                -> {
            LinkedHashMap<String, String> ret = new LinkedHashMap<>();
            ret.put(resultSet.getString("author"), resultSet.getString("sum"));
            return ret;
        }, character);
    }


}
