package dev.chijiokeibekwe.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.chijiokeibekwe.librarymanagementsystem.annotation.WithMockUser;
import dev.chijiokeibekwe.librarymanagementsystem.auth.CustomUserDetailsService;
import dev.chijiokeibekwe.librarymanagementsystem.auth.DelegatedAuthenticationEntryPoint;
import dev.chijiokeibekwe.librarymanagementsystem.config.SecurityConfig;
import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookBorrowingRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookReturnRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BorrowingRecordResponse;
import dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus;
import dev.chijiokeibekwe.librarymanagementsystem.service.BorrowingRecordService;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@EnableConfigurationProperties(value = {RsaKeyProperties.class})
@WebMvcTest(value = BorrowingRecordController.class)
class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BorrowingRecordController borrowingRecordController;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    private final TestUtil testUtil = new TestUtil();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .findAndRegisterModules();

    @Test
    @WithMockUser
    void testRecordBookBorrowing() throws Exception {
        BorrowingRecordResponse response = testUtil.getBorrowingRecordResponse();
        BookBorrowingRequest request = new BookBorrowingRequest(
                LocalDate.of(2022, 7, 12),
                LocalDate.of(2022, 7, 19)
        );

        when(borrowingRecordService.recordBookBorrowing(2L, 2L, request)).thenReturn(response);

        this.mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 2, 2)
                        .content(
                                "{" +
                                "\"borrowingDate\":\"12-07-2022\"," +
                                "\"dueDate\":\"19-07-2022\"" +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Book borrowing recorded successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("12-07-2022 09:32"))
                .andExpect(jsonPath("$.data.borrowingDate").value("12-07-2022"))
                .andExpect(jsonPath("$.data.dueDate").value("19-07-2022"))
                .andExpect(jsonPath("$.data.returnDate").isEmpty())
                .andExpect(jsonPath("$.data.book.id").value(response.getBook().getId()))
                .andExpect(jsonPath("$.data.book.createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.book.title").value(response.getBook().getTitle()))
                .andExpect(jsonPath("$.data.book.author").value(response.getBook().getAuthor()))
                .andExpect(jsonPath("$.data.book.publicationYear").value(response.getBook().getPublicationYear()))
                .andExpect(jsonPath("$.data.book.isbn").value(response.getBook().getIsbn()))
                .andExpect(jsonPath("$.data.book.status").value(response.getBook().getStatus().getValue()))
                .andExpect(jsonPath("$.data.patron.id").value(response.getPatron().getId()))
                .andExpect(jsonPath("$.data.patron.createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.patron.firstName").value(response.getPatron().getFirstName()))
                .andExpect(jsonPath("$.data.patron.lastName").value(response.getPatron().getLastName()))
                .andExpect(jsonPath("$.data.patron.contact.phoneNumber").value(response.getPatron().getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.patron.contact.email").value(response.getPatron().getContact().getEmail()))
                .andExpect(jsonPath("$.data.patron.address.streetAddress").value(response.getPatron().getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.patron.address.city").value(response.getPatron().getAddress().getCity()))
                .andExpect(jsonPath("$.data.patron.address.state").value(response.getPatron().getAddress().getState()))
                .andExpect(jsonPath("$.data.patron.address.country").value(response.getPatron().getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testRecordBookBorrowing_whenDueDateIsNull() throws Exception {

        this.mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 2, 2)
                        .content(
                                "{" +
                                "\"borrowingDate\":\"12-07-2022\"," +
                                "\"dueDate\":null" +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Due date is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testRecordBookReturn() throws Exception {
        BorrowingRecordResponse response = testUtil.getBorrowingRecordResponse();
        BookReturnRequest request = new BookReturnRequest(
                LocalDate.of(2022, 7, 20)
        );

        when(borrowingRecordService.recordBookReturn(2L, 2L, request)).thenReturn(response);

        this.mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 2, 2)
                        .content(
                                "{" +
                                "\"returnDate\":\"20-07-2022\"" +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Book return recorded successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("12-07-2022 09:32"))
                .andExpect(jsonPath("$.data.borrowingDate").value("12-07-2022"))
                .andExpect(jsonPath("$.data.dueDate").value("19-07-2022"))
                .andExpect(jsonPath("$.data.returnDate").isEmpty())
                .andExpect(jsonPath("$.data.book.id").value(response.getBook().getId()))
                .andExpect(jsonPath("$.data.book.createdAt").value("21-10-2023 11:15"))
                .andExpect(jsonPath("$.data.book.title").value(response.getBook().getTitle()))
                .andExpect(jsonPath("$.data.book.author").value(response.getBook().getAuthor()))
                .andExpect(jsonPath("$.data.book.publicationYear").value(response.getBook().getPublicationYear()))
                .andExpect(jsonPath("$.data.book.isbn").value(response.getBook().getIsbn()))
                .andExpect(jsonPath("$.data.book.status").value(response.getBook().getStatus().getValue()))
                .andExpect(jsonPath("$.data.patron.id").value(response.getPatron().getId()))
                .andExpect(jsonPath("$.data.patron.createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.patron.firstName").value(response.getPatron().getFirstName()))
                .andExpect(jsonPath("$.data.patron.lastName").value(response.getPatron().getLastName()))
                .andExpect(jsonPath("$.data.patron.contact.phoneNumber").value(response.getPatron().getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.patron.contact.email").value(response.getPatron().getContact().getEmail()))
                .andExpect(jsonPath("$.data.patron.address.streetAddress").value(response.getPatron().getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.patron.address.city").value(response.getPatron().getAddress().getCity()))
                .andExpect(jsonPath("$.data.patron.address.state").value(response.getPatron().getAddress().getState()))
                .andExpect(jsonPath("$.data.patron.address.country").value(response.getPatron().getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testRecordBookReturn_whenReturnDateIsNull() throws Exception {

        this.mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 2, 2)
                        .content(
                                "{" +
                                "\"returnDate\":null" +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Return date is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
