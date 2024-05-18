package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePatronRequest(
        @NotNull(message = "First name is required")
        String firstName,

        @NotNull(message = "Last name is required")
        String lastName,

        @NotNull(message = "Contact is required")
        ContactDetailsRequest contact,

        @NotNull(message = "Address is required")
        AddressRequest address
)
{
    //
}
