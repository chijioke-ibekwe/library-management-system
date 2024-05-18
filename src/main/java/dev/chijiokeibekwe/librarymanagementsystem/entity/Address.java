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
public class Address {

    private String streetAddress;

    private String city;

    @NotNull(message = "State is missing on patron")
    private String state;

    @NotNull(message = "Country is missing on patron")
    private String country;
}
