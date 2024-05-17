package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookBorrowingRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookReturnRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BorrowingRecordResponse;

public interface BorrowingRecordService {

    BorrowingRecordResponse recordBookBorrowing(Long bookId, Long patronId, BookBorrowingRequest bookBorrowingRequest);

    BorrowingRecordResponse recordBookReturn(Long bookId, Long patronId, BookReturnRequest bookReturnRequest);
}
