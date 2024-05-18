package dev.chijiokeibekwe.librarymanagementsystem.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDetails {

    @NotNull(message = "Phone number is missing on patron")
    private String phoneNumber;

    private String email;
}
