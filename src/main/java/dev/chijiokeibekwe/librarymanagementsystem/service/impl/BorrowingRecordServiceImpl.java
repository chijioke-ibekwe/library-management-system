package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookBorrowingRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookReturnRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BorrowingRecordResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.entity.BorrowingRecord;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import dev.chijiokeibekwe.librarymanagementsystem.exception.BookUnavailableException;
import dev.chijiokeibekwe.librarymanagementsystem.mapper.Mapper;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BookRepository;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BorrowingRecordRepository;
import dev.chijiokeibekwe.librarymanagementsystem.repository.PatronRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.BorrowingRecordService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;

    private final BookRepository bookRepository;

    private final PatronRepository patronRepository;

    @Override
    @Transactional
    @CacheEvict(value="book_details", key="#bookId")
    public BorrowingRecordResponse recordBookBorrowing(Long bookId, Long patronId, BookBorrowingRequest bookBorrowingRequest) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if(book.getStatus().equals(BookStatus.UNAVAILABLE)) {
            throw new BookUnavailableException(String.format("Book with title '%s' is not available for borrowing", book.getTitle()));
        }

        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new EntityNotFoundException("Patron not found"));

        book.setStatus(BookStatus.UNAVAILABLE);
        bookRepository.save(book);

        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .borrowingDate(bookBorrowingRequest.borrowingDate())
                .dueDate(bookBorrowingRequest.dueDate())
                .status(BorrowingRecordStatus.OPEN)
                .book(book)
                .patron(patron)
                .build();

        return Mapper.toBorrowingRecordResponse(borrowingRecordRepository.save(borrowingRecord));
    }

    @Override
    @Transactional
    @CacheEvict(value="book_details", key="#bookId")
    public BorrowingRecordResponse recordBookReturn(Long bookId, Long patronId, BookReturnRequest bookReturnRequest) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findByBookIdAndPatronIdAndStatus(bookId, patronId, BorrowingRecordStatus.OPEN)
                .orElseThrow(() -> new EntityNotFoundException("No open borrowing record was found for the specified book and patron"));

        Book book = borrowingRecord.getBook();

        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        borrowingRecord.setReturnDate(bookReturnRequest.returnDate());
        borrowingRecord.setStatus(BorrowingRecordStatus.CLOSED);

        return Mapper.toBorrowingRecordResponse(borrowingRecordRepository.save(borrowingRecord));
    }
}
