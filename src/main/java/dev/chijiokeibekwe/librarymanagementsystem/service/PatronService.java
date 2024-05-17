package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatronService {

    Page<PatronResponse> getAllPatrons(Pageable pageable);

    PatronResponse getPatron(Long patronId);

    PatronResponse createPatron(CreatePatronRequest createPatronRequest);

    PatronResponse updatePatron(Long patronId, UpdatePatronRequest updatePatronRequest);

    void deletePatron(Long patronId);
}
