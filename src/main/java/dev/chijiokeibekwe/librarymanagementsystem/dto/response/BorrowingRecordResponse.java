package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordResponse {

    private Long id;

    private LocalDate borrowingDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private BorrowingRecordStatus status;

    private BookResponse book;

    private PatronResponse patron;
}
