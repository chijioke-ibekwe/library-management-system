package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookReturnRequest(
        @NotNull(message = "Return date is required")
        LocalDate returnDate
)
{
    //
}
