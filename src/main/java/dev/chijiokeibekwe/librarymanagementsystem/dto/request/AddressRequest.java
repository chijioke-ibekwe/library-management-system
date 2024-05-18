package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        String streetAddress,

        String city,

        @NotNull(message = "Address state is required")
        String state,

        @NotNull(message = "Address country is required")
        String country
)
{
    //
}