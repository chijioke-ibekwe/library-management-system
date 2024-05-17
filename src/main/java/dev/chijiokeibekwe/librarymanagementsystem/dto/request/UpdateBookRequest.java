package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import dev.chijiokeibekwe.librarymanagementsystem.annotation.PastOrPresentYear;
import jakarta.validation.constraints.NotNull;

public record UpdateBookRequest(

        @NotNull(message = "Author is required")
        String author,

        @NotNull(message = "Publication year is required")
        @PastOrPresentYear(message = "Publication year has to be in the past or present")
        Integer publicationYear,

        @NotNull(message = "ISBN is required")
        String isbn
)
{
    //
}
