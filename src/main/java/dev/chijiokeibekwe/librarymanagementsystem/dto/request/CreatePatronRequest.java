package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePatronRequest(
        @NotNull(message = "First name is required")
        String firstName,

        @NotNull(message = "Last name is required")
        String lastName,

        ContactDetailsRequest contact,

        AddressRequest address
)
{
    //
}
