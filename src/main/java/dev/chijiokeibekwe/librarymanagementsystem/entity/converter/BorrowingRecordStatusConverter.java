package dev.chijiokeibekwe.librarymanagementsystem.entity.converter;

import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class BorrowingRecordStatusConverter implements AttributeConverter<BorrowingRecordStatus, String> {

    @Override
    public String convertToDatabaseColumn(BorrowingRecordStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public BorrowingRecordStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return Stream.of(BorrowingRecordStatus.values())
                .filter(s -> s.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
