package dev.chijiokeibekwe.librarymanagementsystem.entity.converter;

import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleNameConverter implements AttributeConverter<RoleName, String> {

    @Override
    public String convertToDatabaseColumn(RoleName roleName) {
        if (roleName == null) {
            return null;
        }
        return roleName.getValue();
    }

    @Override
    public RoleName convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return Stream.of(RoleName.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
