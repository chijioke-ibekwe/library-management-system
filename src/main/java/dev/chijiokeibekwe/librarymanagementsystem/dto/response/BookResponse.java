package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateTimeSerializer;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long id;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    private String title;

    private String author;

    private Integer publicationYear;

    private String isbn;

    private BookStatus status;
}