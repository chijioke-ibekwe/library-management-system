package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Address;
import dev.chijiokeibekwe.librarymanagementsystem.entity.ContactDetails;
import jakarta.validation.constraints.NotNull;

public record CreatePatronRequest(
        @NotNull(message = "First name is required")
        String firstName,

        @NotNull(message = "Last name is required")
        String lastName,

        ContactDetails contact,

        Address address
)
{
    //
}
