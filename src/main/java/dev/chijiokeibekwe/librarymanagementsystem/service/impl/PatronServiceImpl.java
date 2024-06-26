package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdatePatronRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Address;
import dev.chijiokeibekwe.librarymanagementsystem.entity.ContactDetails;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import dev.chijiokeibekwe.librarymanagementsystem.mapper.Mapper;
import dev.chijiokeibekwe.librarymanagementsystem.repository.PatronRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.PatronService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    @Override
    public Page<PatronResponse> getAllPatrons(Pageable pageable) {

        return patronRepository.findAll(pageable).map(Mapper::toPatronResponse);
    }

    @Override
    @Cacheable(value="patron_details", key="#patronId")
    public PatronResponse getPatron(Long patronId) {

        return patronRepository.findById(patronId).map(Mapper::toPatronResponse)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found"));
    }

    @Override
    public PatronResponse createPatron(CreatePatronRequest createPatronRequest) {
        Patron patron = new Patron();
        ContactDetails contact = new ContactDetails();
        Address address = new Address();

        BeanUtils.copyProperties(createPatronRequest, patron);
        BeanUtils.copyProperties(createPatronRequest.contact(), contact);
        BeanUtils.copyProperties(createPatronRequest.address(), address);

        patron.setContact(contact);
        patron.setAddress(address);

        return Mapper.toPatronResponse(patronRepository.save(patron));
    }

    @Override
    @CachePut(value="patron_details", key="#patronId")
    public PatronResponse updatePatron(Long patronId, UpdatePatronRequest updatePatronRequest) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new EntityNotFoundException("Patron not found"));

        ContactDetails contact = new ContactDetails();
        Address address = new Address();
        BeanUtils.copyProperties(updatePatronRequest.contact(), contact);
        BeanUtils.copyProperties(updatePatronRequest.address(), address);

        patron.setContact(contact);
        patron.setAddress(address);

        return Mapper.toPatronResponse(patronRepository.save(patron));
    }

    @Override
    @CacheEvict(value="patron_details", key="#patronId")
    public void deletePatron(Long patronId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new EntityNotFoundException("Patron not found"));

        patronRepository.delete(patron);
    }
}
