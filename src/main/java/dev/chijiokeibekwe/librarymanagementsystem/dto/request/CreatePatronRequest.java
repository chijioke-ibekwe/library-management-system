package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreatePatronRequest(
        @NotNull(message = "First name is required")
        String firstName,

        @NotNull(message = "Last name is required")
        String lastName,

        @Valid
        @NotNull(message = "Contact is required")
        ContactDetailsRequest contact,

        @Valid
        @NotNull(message = "Address is required")
        AddressRequest address
)
{
    //
}
