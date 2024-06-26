package dev.chijiokeibekwe.librarymanagementsystem.entity;

import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord extends BaseEntity {

    @NotNull(message = "Borrowing date is missing on borrowing record")
    private LocalDate borrowingDate;

    @NotNull(message = "Due date is missing on borrowing record")
    private LocalDate dueDate;

    private LocalDate returnDate;

    @NotNull(message = "Status is missing on borrowing record")
    private BorrowingRecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="patron_id", nullable = false)
    private Patron patron;
}
