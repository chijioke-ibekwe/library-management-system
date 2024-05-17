package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookBorrowingRequest(
        @NotNull(message = "Borrowing date is required")
        LocalDate borrowingDate,

        @NotNull(message = "Due date is required")
        LocalDate dueDate
)
{
    //
}
