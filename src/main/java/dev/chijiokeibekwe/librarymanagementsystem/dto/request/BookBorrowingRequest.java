package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateDeserializer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookBorrowingRequest(
        @NotNull(message = "Borrowing date is required")
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        LocalDate borrowingDate,

        @NotNull(message = "Due date is required")
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        LocalDate dueDate
)
{
    //
}
