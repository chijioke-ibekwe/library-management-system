package dev.chijiokeibekwe.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.chijiokeibekwe.librarymanagementsystem.annotation.WithMockUser;
import dev.chijiokeibekwe.librarymanagementsystem.auth.CustomUserDetailsService;
import dev.chijiokeibekwe.librarymanagementsystem.auth.DelegatedAuthenticationEntryPoint;
import dev.chijiokeibekwe.librarymanagementsystem.config.SecurityConfig;
import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus;
import dev.chijiokeibekwe.librarymanagementsystem.service.BookService;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@EnableConfigurationProperties(value = {RsaKeyProperties.class})
@WebMvcTest(value = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController bookController;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    private final TestUtil testUtil = new TestUtil();

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    @WithMockUser
    void testCreateBook() throws Exception {
        BookResponse response = testUtil.getBookResponse();
        CreateBookRequest request = new CreateBookRequest(
                "Things Fall Apart",
                "Chinua Achebe",
                1991,
                "9123981785"
        );

        when(bookService.createBook(request)).thenReturn(response);

        this.mockMvc.perform(post("/api/books")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Book created successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.publicationYear").value(response.getPublicationYear()))
                .andExpect(jsonPath("$.data.isbn").value(response.getIsbn()))
                .andExpect(jsonPath("$.data.status").value(response.getStatus().getValue()));
    }

    @Test
    @WithMockUser
    void testCreateBook_whenPublicationYearIsInTheFuture() throws Exception {
        CreateBookRequest request = new CreateBookRequest(
                "Things Fall Apart",
                "Chinua Achebe",
                LocalDate.now().getYear() + 5,
                "9123981785"
        );

        this.mockMvc.perform(post("/api/books")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Publication year has to be in the past or present"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testGetAllBooks() throws Exception {
        BookResponse response = testUtil.getBookResponse();

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(response)));

        this.mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.data.content[0].id").value(response.getId()))
                .andExpect(jsonPath("$.data.content[0].createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.content[0].title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.content[0].author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.content[0].publicationYear").value(response.getPublicationYear()))
                .andExpect(jsonPath("$.data.content[0].isbn").value(response.getIsbn()))
                .andExpect(jsonPath("$.data.content[0].status").value(response.getStatus().getValue()));
    }

    @Test
    @WithMockUser
    void testGetBook() throws Exception {
        BookResponse response = testUtil.getBookResponse();

        when(bookService.getBook(2L)).thenReturn(response);

        this.mockMvc.perform(get("/api/books/{bookId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.publicationYear").value(response.getPublicationYear()))
                .andExpect(jsonPath("$.data.isbn").value(response.getIsbn()))
                .andExpect(jsonPath("$.data.status").value(response.getStatus().getValue()));
    }

    @Test
    @WithMockUser
    void testUpdateBook() throws Exception {
        UpdateBookRequest request = new UpdateBookRequest(
                "Things Fall Apart",
                "Chinua Achebe",
                1992,
                "9123981785"
        );
        BookResponse response = testUtil.getBookResponse();

        when(bookService.updateBook(2L, request)).thenReturn(response);

        this.mockMvc.perform(put("/api/books/{bookId}", 2)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.data.publicationYear").value(response.getPublicationYear()))
                .andExpect(jsonPath("$.data.isbn").value(response.getIsbn()))
                .andExpect(jsonPath("$.data.status").value(response.getStatus().getValue()));
    }

    @Test
    @WithMockUser
    void testUpdateBook_whenPublicationYearIsInTheFuture() throws Exception {
        UpdateBookRequest request = new UpdateBookRequest(
                "Things Fall Apart",
                "Chinua Achebe",
                LocalDate.now().getYear() + 5,
                "9123981785"
        );

        this.mockMvc.perform(put("/api/books/{bookId}", 2)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Publication year has to be in the past or present"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testDeleteBook() throws Exception {

        this.mockMvc.perform(delete("/api/books/{bookId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Book deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
