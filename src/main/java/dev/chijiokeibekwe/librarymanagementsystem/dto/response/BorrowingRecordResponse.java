package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateSerializer;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateTimeSerializer;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordResponse {

    private Long id;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate borrowingDate;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate dueDate;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate returnDate;

    private BorrowingRecordStatus status;

    private BookResponse book;

    private PatronResponse patron;
}
