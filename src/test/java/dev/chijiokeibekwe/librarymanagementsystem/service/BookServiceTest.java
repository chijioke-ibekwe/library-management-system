package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BookRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.impl.BookServiceImpl;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookServiceImpl.class})
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testCreateBook() {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        CreateBookRequest request = new CreateBookRequest(
                "Things Fall Apart",
                "Chinua Achebe",
                1991,
                "9123981785"
        );

        when(bookRepository.save(any(Book.class))).thenReturn(testUtil.getBook());

        BookResponse response = bookService.createBook(request);

        verify(bookRepository, times(1)).save(bookArgumentCaptor.capture());

        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("Things Fall Apart");
        assertThat(bookArgumentCaptor.getValue().getAuthor()).isEqualTo("Chinua Achebe");
        assertThat(bookArgumentCaptor.getValue().getPublicationYear()).isEqualTo(1991);
        assertThat(bookArgumentCaptor.getValue().getIsbn()).isEqualTo("9123981785");
        assertThat(bookArgumentCaptor.getValue().getStatus()).isEqualTo(BookStatus.AVAILABLE);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 21, 11, 15));
        assertThat(response.getTitle()).isEqualTo("Things Fall Apart");
        assertThat(response.getAuthor()).isEqualTo("Chinua Achebe");
        assertThat(response.getPublicationYear()).isEqualTo(1991);
        assertThat(response.getIsbn()).isEqualTo("9123981785");
        assertThat(response.getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(testUtil.getBook())));

        Page<BookResponse> response = bookService.getAllBooks(Pageable.ofSize(10));

        assertThat(response.getContent().get(0).getId()).isEqualTo(2L);
        assertThat(response.getContent().get(0).getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 21, 11, 15));
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("Things Fall Apart");
        assertThat(response.getContent().get(0).getAuthor()).isEqualTo("Chinua Achebe");
        assertThat(response.getContent().get(0).getPublicationYear()).isEqualTo(1991);
        assertThat(response.getContent().get(0).getIsbn()).isEqualTo("9123981785");
        assertThat(response.getContent().get(0).getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    public void testGetBook() {
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getBook()));

        BookResponse response = bookService.getBook(2L);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 10, 21, 11, 15));
        assertThat(response.getTitle()).isEqualTo("Things Fall Apart");
        assertThat(response.getAuthor()).isEqualTo("Chinua Achebe");
        assertThat(response.getPublicationYear()).isEqualTo(1991);
        assertThat(response.getIsbn()).isEqualTo("9123981785");
        assertThat(response.getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    public void testGetUser_whenBookNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBook(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    public void testUpdateBook() {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        UpdateBookRequest request = new UpdateBookRequest(
                "There was a Country",
                "Chinua Achebe",
                1982,
                "67818170232"
        );

        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getBook()));
        when(bookRepository.save(any(Book.class))).thenReturn(testUtil.getBook());

        bookService.updateBook(2L, request);

        verify(bookRepository, times(1)).save(bookArgumentCaptor.capture());

        assertThat(bookArgumentCaptor.getValue().getCreatedAt()).isNotNull();
        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("There was a Country");
        assertThat(bookArgumentCaptor.getValue().getAuthor()).isEqualTo("Chinua Achebe");
        assertThat(bookArgumentCaptor.getValue().getPublicationYear()).isEqualTo(1982);
        assertThat(bookArgumentCaptor.getValue().getIsbn()).isEqualTo("67818170232");
        assertThat(bookArgumentCaptor.getValue().getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    public void testUpdateBook_whenBookNotFound() {
        UpdateBookRequest request = new UpdateBookRequest(
                "There was a Country",
                "Chinua Achebe",
                1982,
                "67818170232"
        );

        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        verifyNoInteractions(bookRepository);

        assertThatThrownBy(() -> bookService.updateBook(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    public void testDeleteBook() {
        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getBook()));

        bookService.deleteBook(2L);

        verify(bookRepository, times(1)).delete(testUtil.getBook());
    }

    @Test
    public void testDeleteBook_whenBookNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");
    }
}