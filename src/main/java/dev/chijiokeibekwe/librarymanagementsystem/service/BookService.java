package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookResponse> getAllBooks(Pageable pageable);

    BookResponse getBook(Long bookId);

    BookResponse createBook(CreateBookRequest createBookRequest);

    BookResponse updateBook(Long bookId, UpdateBookRequest updateBookRequest);

    void deleteBook(Long bookId);
}
