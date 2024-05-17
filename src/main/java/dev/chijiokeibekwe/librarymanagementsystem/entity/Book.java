package dev.chijiokeibekwe.librarymanagementsystem.entity;

import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import jakarta.persistence.*;
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
@Table(name = "books")
public class Book extends BaseEntity {

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private String author;

    @NotNull(message = "Publication year is required")
    private Integer publicationYear;

    @NotNull(message = "ISBN is required")
    private String isbn;

    private BookStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
}
