package dev.chijiokeibekwe.librarymanagementsystem.entity.converter;

import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, String> {

    @Override
    public String convertToDatabaseColumn(BookStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public BookStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return Stream.of(BookStatus.values())
                .filter(s -> s.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
