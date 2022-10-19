package org.test.task;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.test.task.entity.Book;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>();

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQL::getUsername);
        registry.add("spring.datasource.password", postgreSQL::getPassword);
    }

    @Test
    void contextLoads() {

    }

    @Test
    void getAllByAlphabetOrder() throws Exception {
        String response = this.mockMvc.perform(get("/api/books/byTitle")).andReturn().getResponse().getContentAsString();
        List<Book> books = objectMapper.readValue(response, new TypeReference<List<Book>>() {
        });
        assertThat(books.get(0).getTitle(), is("War and Peace"));
        assertThat(books.get(books.size() - 1).getTitle(), is("Anna Karenina"));
    }

    @Test
    void getGroupByAuthor() throws Exception {
        String response = this.mockMvc.perform(get("/api/books/byAuthor")).andReturn().getResponse().getContentAsString();
        Map<String, List<Book>> books = objectMapper.readValue(response, new TypeReference<>() {
        });
        assertThat(books.get("L. Tolstoy").size(), is(2));
        assertThat(books.get("N. Gogol").size(), is(1));
        assertThat(books.get("F. Dostoevsky").size(), is(2));
    }

    @Test
    void createBookTest() throws Exception {
        Book book = new Book();
        book.setAuthor("test");
        book.setDescription("test");
        book.setTitle("test");
        String json = objectMapper.writeValueAsString(book);
        String response = this.mockMvc.perform(post("/api/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn().getResponse().getContentAsString();
        Book returnVal = objectMapper.readValue(response, Book.class);
        assertThat(returnVal.getTitle(), is("test"));
        assertThat(returnVal.getDescription(), is("test"));
        assertThat(returnVal.getAuthor(), is("test"));
    }

    @Test
    void FindByMostFrequentCharTest() throws Exception {
        FindByMostFreqChar("a");
        FindByMostFreqChar("A");
    }

    private void FindByMostFreqChar(String character) throws Exception {
        String response = this.mockMvc.perform(get("/api/books/byMostFrequentChar?character=" + character)).andReturn().getResponse().getContentAsString();
        List<Map<String, String>> books = objectMapper.readValue(response, new TypeReference<>() {
        });
        Map<String, String> expected1 = new HashMap<>();
        expected1.put("L. Tolstoy", "7");
        Map<String, String> expected2 = new HashMap<>();
        expected2.put("F. Dostoevsky", "4");
        Map<String, String> expected3 = new HashMap<>();
        expected3.put("N. Gogol", "1");
        assertThat(books.get(0), is(expected1));
        assertThat(books.get(1), is(expected2));
        assertThat(books.get(2), is(expected3));
    }
}
