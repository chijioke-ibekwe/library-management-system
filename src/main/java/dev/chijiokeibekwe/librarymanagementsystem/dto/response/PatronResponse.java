package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.chijiokeibekwe.librarymanagementsystem.dto.serializers.CustomLocalDateTimeSerializer;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Address;
import dev.chijiokeibekwe.librarymanagementsystem.entity.ContactDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronResponse {

    private Long id;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

    private ContactDetails contact;

    private Address address;
}