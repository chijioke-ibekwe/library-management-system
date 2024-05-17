package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long id;

    private String title;

    private String author;

    private LocalDate publicationYear;

    private String isbn;
}