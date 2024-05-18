package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        String streetAddress,

        String city,

        @NotNull(message = "State in address is required")
        String state,

        @NotNull(message = "Country in address is required")
        String country
)
{
    //
}