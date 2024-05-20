package dev.chijiokeibekwe.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.chijiokeibekwe.librarymanagementsystem.annotation.WithMockUser;
import dev.chijiokeibekwe.librarymanagementsystem.auth.CustomUserDetailsService;
import dev.chijiokeibekwe.librarymanagementsystem.auth.DelegatedAuthenticationEntryPoint;
import dev.chijiokeibekwe.librarymanagementsystem.config.SecurityConfig;
import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.*;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus;
import dev.chijiokeibekwe.librarymanagementsystem.service.PatronService;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@EnableConfigurationProperties(value = {RsaKeyProperties.class})
@WebMvcTest(value = PatronController.class)
class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatronController patronController;

    @MockBean
    private PatronService patronService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    private final TestUtil testUtil = new TestUtil();

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Test
    @WithMockUser
    void testCreatePatron() throws Exception {
        PatronResponse response = testUtil.getPatronResponse();
        CreatePatronRequest request = new CreatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        when(patronService.createPatron(request)).thenReturn(response);

        this.mockMvc.perform(post("/api/patrons")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Patron created successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.data.contact.phoneNumber").value(response.getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.contact.email").value(response.getContact().getEmail()))
                .andExpect(jsonPath("$.data.address.streetAddress").value(response.getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.address.city").value(response.getAddress().getCity()))
                .andExpect(jsonPath("$.data.address.state").value(response.getAddress().getState()))
                .andExpect(jsonPath("$.data.address.country").value(response.getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testCreatePatron_whenContactPhoneNumberIsNull() throws Exception {
        CreatePatronRequest request = new CreatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest(null, "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        this.mockMvc.perform(post("/api/patrons")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Contact phone number is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testCreatePatron_whenAddressStateIsNull() throws Exception {
        CreatePatronRequest request = new CreatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", null, "Nigeria")
        );

        this.mockMvc.perform(post("/api/patrons")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Address state is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testCreatePatron_whenAddressCountryIsNull() throws Exception {
        CreatePatronRequest request = new CreatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", null)
        );

        this.mockMvc.perform(post("/api/patrons")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Address country is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testGetAllPatrons() throws Exception {
        PatronResponse response = testUtil.getPatronResponse();

        when(patronService.getAllPatrons(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(response)));

        this.mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.data.content[0].id").value(response.getId()))
                .andExpect(jsonPath("$.data.content[0].createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.content[0].firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.data.content[0].lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.data.content[0].contact.phoneNumber").value(response.getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.content[0].contact.email").value(response.getContact().getEmail()))
                .andExpect(jsonPath("$.data.content[0].address.streetAddress").value(response.getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.content[0].address.city").value(response.getAddress().getCity()))
                .andExpect(jsonPath("$.data.content[0].address.state").value(response.getAddress().getState()))
                .andExpect(jsonPath("$.data.content[0].address.country").value(response.getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testGetPatron() throws Exception {
        PatronResponse response = testUtil.getPatronResponse();

        when(patronService.getPatron(2L)).thenReturn(response);

        this.mockMvc.perform(get("/api/patrons/{patronId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.data.contact.phoneNumber").value(response.getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.contact.email").value(response.getContact().getEmail()))
                .andExpect(jsonPath("$.data.address.streetAddress").value(response.getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.address.city").value(response.getAddress().getCity()))
                .andExpect(jsonPath("$.data.address.state").value(response.getAddress().getState()))
                .andExpect(jsonPath("$.data.address.country").value(response.getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testUpdatePatron() throws Exception {
        UpdatePatronRequest request = new UpdatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );
        PatronResponse response = testUtil.getPatronResponse();

        when(patronService.updatePatron(2L, request)).thenReturn(response);

        this.mockMvc.perform(put("/api/patrons/{patronId}", 2)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Patron updated successfully"))
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.createdAt").value("18-05-2023 15:22"))
                .andExpect(jsonPath("$.data.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(response.getLastName()))
                .andExpect(jsonPath("$.data.contact.phoneNumber").value(response.getContact().getPhoneNumber()))
                .andExpect(jsonPath("$.data.contact.email").value(response.getContact().getEmail()))
                .andExpect(jsonPath("$.data.address.streetAddress").value(response.getAddress().getStreetAddress()))
                .andExpect(jsonPath("$.data.address.city").value(response.getAddress().getCity()))
                .andExpect(jsonPath("$.data.address.state").value(response.getAddress().getState()))
                .andExpect(jsonPath("$.data.address.country").value(response.getAddress().getCountry()));
    }

    @Test
    @WithMockUser
    void testUpdatePatron_whenFirstNameIsNull() throws Exception {
        UpdatePatronRequest request = new UpdatePatronRequest(
                null,
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        this.mockMvc.perform(put("/api/patrons/{patronId}", 2)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("First name is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testUpdatePatron_whenLastNameIsNull() throws Exception {
        UpdatePatronRequest request = new UpdatePatronRequest(
                "Peter",
                null,
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        this.mockMvc.perform(put("/api/patrons/{patronId}", 2)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ResponseStatus.FAILED.getValue()))
                .andExpect(jsonPath("$.message").value("Last name is required"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser
    void testDeletePatron() throws Exception {

        this.mockMvc.perform(delete("/api/patrons/{patronId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ResponseStatus.SUCCESSFUL.getValue()))
                .andExpect(jsonPath("$.message").value("Patron deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
