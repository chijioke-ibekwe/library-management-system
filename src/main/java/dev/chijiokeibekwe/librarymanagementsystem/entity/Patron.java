package dev.chijiokeibekwe.librarymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patrons")
public class Patron extends BaseEntity {

    @NotNull(message = "First name is missing on patron")
    private String firstName;

    @NotNull(message = "Last name is missing on patron")
    private String lastName;

    @Valid
    @Embedded
    private ContactDetails contact;

    @Valid
    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patron", orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
}
