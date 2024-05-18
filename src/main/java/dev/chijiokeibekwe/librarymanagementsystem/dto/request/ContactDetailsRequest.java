package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ContactDetailsRequest(
        @NotNull(message = "Contact phone number is required")
        String phoneNumber,

        String email
)
{
    //
}