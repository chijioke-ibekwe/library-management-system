package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookBorrowingRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookReturnRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BorrowingRecordResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.entity.BorrowingRecord;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import dev.chijiokeibekwe.librarymanagementsystem.exception.BookUnavailableException;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BookRepository;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BorrowingRecordRepository;
import dev.chijiokeibekwe.librarymanagementsystem.repository.PatronRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.impl.BorrowingRecordServiceImpl;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BorrowingRecordServiceImpl.class})
public class BorrowingRecordServiceTest {
    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @MockBean
    private BorrowingRecordRepository borrowingRecordRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private PatronRepository patronRepository;

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testRecordBookBorrowing() {
        ArgumentCaptor<BorrowingRecord> borrowingRecordArgumentCaptor = ArgumentCaptor.forClass(BorrowingRecord.class);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        BookBorrowingRequest request = new BookBorrowingRequest(
                LocalDate.of(2022, 7, 12),
                LocalDate.of(2022, 7, 19)
        );
        BorrowingRecord borrowingRecord = testUtil.getBorrowingRecord();
        borrowingRecord.getBook().setStatus(BookStatus.UNAVAILABLE);

        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getBook()));
        when(patronRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getPatron()));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordResponse response = borrowingRecordService.recordBookBorrowing(2L, 2L, request);

        verify(borrowingRecordRepository, times(1)).save(borrowingRecordArgumentCaptor.capture());
        verify(bookRepository, times(1)).save(bookArgumentCaptor.capture());

        assertThat(borrowingRecordArgumentCaptor.getValue().getBorrowingDate()).isEqualTo(LocalDate.of(2022, 7, 12));
        assertThat(borrowingRecordArgumentCaptor.getValue().getDueDate()).isEqualTo(LocalDate.of(2022, 7, 19));
        assertThat(borrowingRecordArgumentCaptor.getValue().getStatus()).isEqualTo(BorrowingRecordStatus.OPEN);
        assertThat(borrowingRecordArgumentCaptor.getValue().getPatron()).isEqualTo(testUtil.getPatron());
        assertThat(bookArgumentCaptor.getValue().getStatus()).isEqualTo(BookStatus.UNAVAILABLE);

        assertThat(response.getBorrowingDate()).isEqualTo(LocalDate.of(2022, 7, 12));
        assertThat(response.getDueDate()).isEqualTo(LocalDate.of(2022, 7, 19));
        assertThat(response.getStatus()).isEqualTo(BorrowingRecordStatus.OPEN);
        assertThat(response.getBook().getStatus()).isEqualTo(BookStatus.UNAVAILABLE);
        assertThat(response.getPatron()).isEqualTo(testUtil.getPatronResponse());
    }

    @Test
    public void testRecordBookBorrowing_whenBookIsUnavailable() {
        BookBorrowingRequest request = new BookBorrowingRequest(
                LocalDate.of(2022, 7, 12),
                LocalDate.of(2022, 7, 19)
        );
        Book book = testUtil.getBook();
        book.setStatus(BookStatus.UNAVAILABLE);

        when(bookRepository.findById(2L)).thenReturn(Optional.ofNullable(book));
        when(patronRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getPatron()));

        verifyNoInteractions(borrowingRecordRepository);

        assertThatThrownBy(() -> borrowingRecordService.recordBookBorrowing(2L, 2L, request))
                .isInstanceOf(BookUnavailableException.class)
                .hasMessage("Book with title 'Things Fall Apart' is not available for borrowing");
    }

    @Test
    public void testRecordBookReturn() {
        ArgumentCaptor<BorrowingRecord> borrowingRecordArgumentCaptor = ArgumentCaptor.forClass(BorrowingRecord.class);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        BookReturnRequest request = new BookReturnRequest(
                LocalDate.of(2022, 7, 20)
        );

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndStatus(2L, 2L, BorrowingRecordStatus.OPEN))
                .thenReturn(Optional.ofNullable(testUtil.getBorrowingRecord()));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(testUtil.getBorrowingRecord());

        BorrowingRecordResponse response = borrowingRecordService.recordBookReturn(2L, 2L, request);

        verify(borrowingRecordRepository, times(1)).save(borrowingRecordArgumentCaptor.capture());
        verify(bookRepository, times(1)).save(bookArgumentCaptor.capture());

        assertThat(borrowingRecordArgumentCaptor.getValue().getBorrowingDate()).isEqualTo(LocalDate.of(2022, 7, 12));
        assertThat(borrowingRecordArgumentCaptor.getValue().getDueDate()).isEqualTo(LocalDate.of(2022, 7, 19));
        assertThat(borrowingRecordArgumentCaptor.getValue().getStatus()).isEqualTo(BorrowingRecordStatus.CLOSED);
        assertThat(borrowingRecordArgumentCaptor.getValue().getBook()).isEqualTo(testUtil.getBook());
        assertThat(borrowingRecordArgumentCaptor.getValue().getPatron()).isEqualTo(testUtil.getPatron());
        assertThat(bookArgumentCaptor.getValue().getStatus()).isEqualTo(BookStatus.AVAILABLE);

        assertThat(response.getBorrowingDate()).isEqualTo(LocalDate.of(2022, 7, 12));
        assertThat(response.getDueDate()).isEqualTo(LocalDate.of(2022, 7, 19));
        assertThat(response.getStatus()).isEqualTo(BorrowingRecordStatus.OPEN);
        assertThat(response.getBook().getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(response.getPatron()).isEqualTo(testUtil.getPatronResponse());
    }

    @Test
    public void testRecordBookReturn_whenOpenBorrowingRecordNotFound() {
        BookReturnRequest request = new BookReturnRequest(
                LocalDate.of(2022, 7, 20)
        );
        BorrowingRecord borrowingRecord = testUtil.getBorrowingRecord();
        borrowingRecord.setStatus(BorrowingRecordStatus.CLOSED);

        verifyNoInteractions(bookRepository);

        assertThatThrownBy(() -> borrowingRecordService.recordBookReturn(2L, 2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("No open borrowing record was found for the specified book and patron");
    }
}