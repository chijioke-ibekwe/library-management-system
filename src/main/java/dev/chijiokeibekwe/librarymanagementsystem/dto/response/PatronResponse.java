package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Address;
import dev.chijiokeibekwe.librarymanagementsystem.entity.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private ContactDetails contact;

    private Address address;
}