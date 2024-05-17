package dev.chijiokeibekwe.librarymanagementsystem.dto.request;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Address;
import dev.chijiokeibekwe.librarymanagementsystem.entity.ContactDetails;

public record UpdatePatronRequest(
        ContactDetails contact,

        Address address
)
{
    //
}
