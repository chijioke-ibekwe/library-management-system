package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ContactDetailsRequest(
        @NotNull(message = "Phone number in contact is required")
        String phoneNumber,

        String email
)
{
    //
}