package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.*;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import dev.chijiokeibekwe.librarymanagementsystem.repository.PatronRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.impl.PatronServiceImpl;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PatronServiceImpl.class})
public class PatronServiceTest {
    @Autowired
    private PatronService patronService;

    @MockBean
    private PatronRepository patronRepository;

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testCreatePatron() {
        ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(Patron.class);
        CreatePatronRequest request = new CreatePatronRequest(
                "Peter",
                "Obi",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        when(patronRepository.save(any(Patron.class))).thenReturn(testUtil.getPatron());

        PatronResponse response = patronService.createPatron(request);

        verify(patronRepository, times(1)).save(patronArgumentCaptor.capture());

        assertThat(patronArgumentCaptor.getValue().getFirstName()).isEqualTo("Peter");
        assertThat(patronArgumentCaptor.getValue().getLastName()).isEqualTo("Obi");
        assertThat(patronArgumentCaptor.getValue().getContact().getPhoneNumber()).isEqualTo("+2347088889999");
        assertThat(patronArgumentCaptor.getValue().getContact().getEmail()).isEqualTo("peter.obi@library.com");
        assertThat(patronArgumentCaptor.getValue().getAddress().getStreetAddress()).isEqualTo("23 Johnson Street");
        assertThat(patronArgumentCaptor.getValue().getAddress().getCity()).isEqualTo("Ikeja");
        assertThat(patronArgumentCaptor.getValue().getAddress().getState()).isEqualTo("Lagos");
        assertThat(patronArgumentCaptor.getValue().getAddress().getCountry()).isEqualTo("Nigeria");

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 5, 18, 15, 22));
        assertThat(response.getFirstName()).isEqualTo("Peter");
        assertThat(response.getLastName()).isEqualTo("Obi");
        assertThat(response.getContact().getPhoneNumber()).isEqualTo("+2347088889999");
        assertThat(response.getContact().getEmail()).isEqualTo("peter.obi@library.com");
        assertThat(response.getAddress().getStreetAddress()).isEqualTo("23 Johnson Street");
        assertThat(response.getAddress().getCity()).isEqualTo("Ikeja");
        assertThat(response.getAddress().getState()).isEqualTo("Lagos");
        assertThat(response.getAddress().getCountry()).isEqualTo("Nigeria");
    }

    @Test
    public void testGetAllPatrons() {
        when(patronRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(testUtil.getPatron())));

        Page<PatronResponse> response = patronService.getAllPatrons(Pageable.ofSize(10));

        assertThat(response.getContent().get(0).getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 5, 18, 15, 22));
        assertThat(response.getContent().get(0).getFirstName()).isEqualTo("Peter");
        assertThat(response.getContent().get(0).getLastName()).isEqualTo("Obi");
        assertThat(response.getContent().get(0).getContact().getPhoneNumber()).isEqualTo("+2347088889999");
        assertThat(response.getContent().get(0).getContact().getEmail()).isEqualTo("peter.obi@library.com");
        assertThat(response.getContent().get(0).getAddress().getStreetAddress()).isEqualTo("23 Johnson Street");
        assertThat(response.getContent().get(0).getAddress().getCity()).isEqualTo("Ikeja");
        assertThat(response.getContent().get(0).getAddress().getState()).isEqualTo("Lagos");
        assertThat(response.getContent().get(0).getAddress().getCountry()).isEqualTo("Nigeria");
    }

    @Test
    public void testGetPatron() {
        when(patronRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getPatron()));

        PatronResponse response = patronService.getPatron(2L);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 5, 18, 15, 22));
        assertThat(response.getFirstName()).isEqualTo("Peter");
        assertThat(response.getLastName()).isEqualTo("Obi");
        assertThat(response.getContact().getPhoneNumber()).isEqualTo("+2347088889999");
        assertThat(response.getContact().getEmail()).isEqualTo("peter.obi@library.com");
        assertThat(response.getAddress().getStreetAddress()).isEqualTo("23 Johnson Street");
        assertThat(response.getAddress().getCity()).isEqualTo("Ikeja");
        assertThat(response.getAddress().getState()).isEqualTo("Lagos");
        assertThat(response.getAddress().getCountry()).isEqualTo("Nigeria");
    }

    @Test
    public void testGetPatron_whenPatronNotFound() {
        when(patronRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patronService.getPatron(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Patron not found");
    }

    @Test
    public void testUpdatePatron() {
        ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(Patron.class);
        UpdatePatronRequest request = new UpdatePatronRequest(
                "Alex",
                "Otti",
                new ContactDetailsRequest("+2347088889998", "peter.obi@library.com"),
                new AddressRequest("23 George Weah Street", "Ikeja", "Lagos", "Nigeria")
        );

        when(patronRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getPatron()));
        when(patronRepository.save(any(Patron.class))).thenReturn(testUtil.getPatron());

        patronService.updatePatron(2L, request);

        verify(patronRepository, times(1)).save(patronArgumentCaptor.capture());

        assertThat(patronArgumentCaptor.getValue().getId()).isEqualTo(2L);
        assertThat(patronArgumentCaptor.getValue().getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 5, 18, 15, 22));
        assertThat(patronArgumentCaptor.getValue().getFirstName()).isEqualTo("Peter");
        assertThat(patronArgumentCaptor.getValue().getLastName()).isEqualTo("Obi");
        assertThat(patronArgumentCaptor.getValue().getContact().getPhoneNumber()).isEqualTo("+2347088889998");
        assertThat(patronArgumentCaptor.getValue().getContact().getEmail()).isEqualTo("peter.obi@library.com");
        assertThat(patronArgumentCaptor.getValue().getAddress().getStreetAddress()).isEqualTo("23 George Weah Street");
        assertThat(patronArgumentCaptor.getValue().getAddress().getCity()).isEqualTo("Ikeja");
        assertThat(patronArgumentCaptor.getValue().getAddress().getState()).isEqualTo("Lagos");
        assertThat(patronArgumentCaptor.getValue().getAddress().getCountry()).isEqualTo("Nigeria");
    }

    @Test
    public void testUpdatePatron_whenPatronNotFound() {
        UpdatePatronRequest request = new UpdatePatronRequest(
                "Alex",
                "Otti",
                new ContactDetailsRequest("+2347088889999", "peter.obi@library.com"),
                new AddressRequest("23 Johnson Street", "Ikeja", "Lagos", "Nigeria")
        );

        when(patronRepository.findById(2L)).thenReturn(Optional.empty());

        verifyNoInteractions(patronRepository);

        assertThatThrownBy(() -> patronService.updatePatron(2L, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Patron not found");
    }

    @Test
    public void testDeletePatron() {
        when(patronRepository.findById(2L)).thenReturn(Optional.ofNullable(testUtil.getPatron()));

        patronService.deletePatron(2L);

        verify(patronRepository, times(1)).delete(testUtil.getPatron());
    }

    @Test
    public void testDeletePatron_whenPatronNotFound() {
        when(patronRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patronService.deletePatron(2L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Patron not found");
    }
}