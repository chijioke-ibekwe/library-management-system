package dev.chijiokeibekwe.librarymanagementsystem.controller;

import dev.chijiokeibekwe.librarymanagementsystem.common.ResponseObject;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.PatronService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus.SUCCESSFUL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    @Operation(summary = "Fetch all patrons", description = "Fetch all patrons of the library")
    @GetMapping
    @PreAuthorize("hasAuthority('patrons:read')")
    public ResponseObject<Page<PatronResponse>> getAllPatrons(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                                  Pageable pageable){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                patronService.getAllPatrons(pageable)
        );
    }

    @Operation(summary = "Fetch a single patron", description = "Fetch a single patron of the library")
    @GetMapping("/{patron_id}")
    @PreAuthorize("hasAuthority('patrons:read')")
    public ResponseObject<PatronResponse> getPatron(@PathVariable("patron_id") Long patronId){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                patronService.getPatron(patronId)
        );
    }

    @Operation(summary = "Create a patron", description = "Create a new patron")
    @PostMapping
    @PreAuthorize("hasAuthority('patrons:write')")
    public ResponseObject<PatronResponse> createPatron(@RequestBody @Valid CreatePatronRequest createPatronRequest){
        log.info("Received request to create the following patron: {}", createPatronRequest);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Patron created successfully",
                patronService.createPatron(createPatronRequest)
        );
    }

    @Operation(summary = "Update a patron", description = "Update an existing patron")
    @PatchMapping("/{patron_id}")
    @PreAuthorize("hasAuthority('patrons:write')")
    public ResponseObject<PatronResponse> updatePatron(@PathVariable("patron_id") Long patronId,
                                                       @RequestBody @Valid UpdatePatronRequest updatePatronRequest){
        log.info("Received request to update patron with ID {} with the following details: {}", patronId, updatePatronRequest);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Patron updated successfully",
                patronService.updatePatron(patronId, updatePatronRequest)
        );
    }

    @Operation(summary = "Delete a patron", description = "Delete an existing patron")
    @DeleteMapping("/{patron_id}")
    @PreAuthorize("hasAuthority('patrons:delete')")
    public ResponseObject<Void> deletePatron(@PathVariable("patron_id") Long patronId){
        log.info("Received request to delete patron with ID {}", patronId);
        patronService.deletePatron(patronId);

        return new ResponseObject<>(
                SUCCESSFUL,
                "Patron deleted successfully",
                null
        );
    }
}
