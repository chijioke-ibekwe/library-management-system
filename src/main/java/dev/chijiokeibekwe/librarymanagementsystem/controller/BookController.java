package dev.chijiokeibekwe.librarymanagementsystem.controller;

import dev.chijiokeibekwe.librarymanagementsystem.common.ResponseObject;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus.SUCCESSFUL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Fetch all books", description = "Fetch all books in the library")
    @GetMapping
    @PreAuthorize("hasAuthority('books:read')")
    public ResponseObject<Page<BookResponse>> getAllBooks(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                              Pageable pageable){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                bookService.getAllBooks(pageable)
        );
    }

    @Operation(summary = "Fetch a single book", description = "Fetch a single book in the library")
    @GetMapping("/{book_id}")
    @PreAuthorize("hasAuthority('books:read')")
    public ResponseObject<BookResponse> getBook(@PathVariable("book_id") Long bookId){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                bookService.getBook(bookId)
        );
    }

    @Operation(summary = "Create a book", description = "Create a new book")
    @PostMapping
    @PreAuthorize("hasAuthority('books:write')")
        public ResponseObject<BookResponse> createBook(@RequestBody @Valid CreateBookRequest createBookRequest){
        log.info("Received request to create the following book: {}", createBookRequest);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Book created successfully",
                bookService.createBook(createBookRequest)
        );
    }

    @Operation(summary = "Update a book", description = "Update an existing book")
    @PatchMapping("/{book_id}")
    @PreAuthorize("hasAuthority('books:write')")
    public ResponseObject<BookResponse> updateBook(@PathVariable("book_id") Long bookId,
                                                   @RequestBody @Valid UpdateBookRequest updateBookRequest){
        log.info("Received request to update book with ID {} with the following details: {}", bookId, updateBookRequest);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Book updated successfully",
                bookService.updateBook(bookId, updateBookRequest)
        );
    }

    @Operation(summary = "Delete a book", description = "Delete an existing book")
    @DeleteMapping("/{book_id}")
    @PreAuthorize("hasAuthority('books:delete')")
    public ResponseObject<Void> deleteBook(@PathVariable("book_id") Long bookId){
        log.info("Received request to delete book with ID {}", bookId);
        bookService.deleteBook(bookId);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Book deleted successfully",
                null
        );
    }
}
