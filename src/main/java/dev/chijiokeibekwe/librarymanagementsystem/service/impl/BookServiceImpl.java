package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import dev.chijiokeibekwe.librarymanagementsystem.mapper.Mapper;
import dev.chijiokeibekwe.librarymanagementsystem.repository.BookRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {

        return bookRepository.findAll(pageable).map(Mapper::toBookResponse);
    }

    @Override
    public BookResponse getBook(Long bookId) {

        return bookRepository.findById(bookId).map(Mapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    @Override
    public BookResponse createBook(CreateBookRequest createBookRequest) {
        Book book = Book.builder()
                .title(createBookRequest.title())
                .author(createBookRequest.author())
                .publicationYear(createBookRequest.publicationYear())
                .isbn(createBookRequest.isbn())
                .status(BookStatus.AVAILABLE)
                .build();

        return Mapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long bookId, UpdateBookRequest updateBookRequest) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        book.setTitle(updateBookRequest.title());
        book.setAuthor(updateBookRequest.author());
        book.setPublicationYear(updateBookRequest.publicationYear());
        book.setIsbn(updateBookRequest.isbn());

        return Mapper.toBookResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        bookRepository.delete(book);
    }
}
