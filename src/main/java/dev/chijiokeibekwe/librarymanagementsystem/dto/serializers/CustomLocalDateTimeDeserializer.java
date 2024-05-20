package dev.chijiokeibekwe.librarymanagementsystem.dto.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        try {
            String dateString = jsonParser.getText().trim();
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        }catch (Exception ex){
            throw new RuntimeException("Date should be in the format 'dd-MM-yyyy HH:mm'");
        }
    }
}
