package dev.chijiokeibekwe.librarymanagementsystem.dto.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        String dateString = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        jsonGenerator.writeString(dateString);
    }
}

