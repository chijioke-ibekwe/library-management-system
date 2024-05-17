package dev.chijiokeibekwe.librarymanagementsystem.controller;

import dev.chijiokeibekwe.librarymanagementsystem.common.ResponseObject;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookBorrowingRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.BookReturnRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BorrowingRecordResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.BorrowingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus.SUCCESSFUL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @Operation(summary = "Record the borrowing of a book", description = "Record the borrowing of a book by a patron")
    @PostMapping("/borrow/{book_id}/patron/{patron_id}")
    @PreAuthorize("hasAuthority('borrowing_records:write')")
    public ResponseObject<BorrowingRecordResponse> recordBookBorrowing(@PathVariable("book_id") Long bookId,
                                                                       @PathVariable("patron_id") Long patronId,
                                                                       @RequestBody @Valid BookBorrowingRequest bookBorrowingRequest){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                borrowingRecordService.recordBookBorrowing(bookId, patronId, bookBorrowingRequest)
        );
    }

    @Operation(summary = "Record the return of a book", description = "Record the return of a book by a patron")
    @PutMapping("/return/{book_id}/patron/{patron_id}")
    @PreAuthorize("hasAuthority('borrowing_records:write')")
    public ResponseObject<BorrowingRecordResponse> recordBookReturn(@PathVariable("book_id") Long bookId,
                                                                    @PathVariable("patron_id") Long patronId,
                                                                    @RequestBody @Valid BookReturnRequest bookReturnRequest){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                borrowingRecordService.recordBookReturn(bookId, patronId, bookReturnRequest)
        );
    }
}
