package org.test.task.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.test.task.entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, List<Book>> groupByAuthor() {
        List<String> authors = jdbcTemplate.query("select author from book", (ResultSet resultSet, int rowNum)
                -> resultSet.getString("author"));
        String whereQuery = "select * from book where author = ? ";
        return authors.stream()
                .collect(Collectors.toMap(author -> author,
                        author -> jdbcTemplate.query(whereQuery, ROW_MAPPER, author),
                        (a, b) -> b));
    }

    public Book save(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = "INSERT INTO book(id, title,author,description) VALUES(default, ?, ?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDescription());
            return ps;
        }, keyHolder);
        return jdbcTemplate.queryForObject("select * from book where id = ? ", ROW_MAPPER, keyHolder.getKeys().get("id"));
    }

    public List<Map<String, String>> getBooksByMostFrequentChar(Character character) {
        String sql = "select author, sum(c) as sum from " +
                "(select author, (select  count(*)  FROM regexp_matches(lower(book.title), lower(?), 'g')) as c" +
                " from book group by author, title) as acS where c > 0 group by author order by sum DESC limit 10;";
        return jdbcTemplate.query(sql, (ResultSet resultSet, int rowNum)
                -> {
            LinkedHashMap<String, String> ret = new LinkedHashMap<>();
            ret.put(resultSet.getString("author"), resultSet.getString("sum"));
            return ret;
        }, character);
    }


}
