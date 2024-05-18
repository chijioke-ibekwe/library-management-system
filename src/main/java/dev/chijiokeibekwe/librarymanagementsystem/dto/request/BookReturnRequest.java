package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateDeserializer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookReturnRequest(
        @NotNull(message = "Return date is required")
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        LocalDate returnDate
)
{
    //
}
