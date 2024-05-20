package dev.chijiokeibekwe.librarymanagementsystem.util;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.AuthenticationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.CreateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UpdateBookRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UserRegistrationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.*;
import dev.chijiokeibekwe.librarymanagementsystem.entity.*;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BookStatus;
import dev.chijiokeibekwe.librarymanagementsystem.enums.BorrowingRecordStatus;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtil {

    public AuthenticationResponse getAuthenticationResponse() {

        return new AuthenticationResponse(
                "e29Y8DYmn0M7j7J89/TQQXMpMQ0GHBaQUDQfJYvNOLLmXEhF9AByXUhY0gzZmTHa",
                "bearer",
                1800
        );
    }

    public AuthenticationRequest getAuthenticationRequest() {

        return new AuthenticationRequest(
                "john.doe@library.com",
                "password"
        );
    }

    public UserRegistrationRequest getUserRegistrationRequest() {

        return new UserRegistrationRequest(
                "John",
                "Doe",
                "john.doe@library.com",
                "password",
                "+2348012345678"
        );
    }

    public UserResponse getUserResponse() {

        return UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe@library.com")
                .phoneNumber("+2348012345678")
                .role(RoleResponse.builder()
                        .id(2L)
                        .name(RoleName.ROLE_USER)
                        .permissions(List.of("users:read"))
                        .build())
                .build();
    }

    public Role getUserRole() {

        return Role.builder()
                .id(1L)
                .description("Role with user permissions")
                .name(RoleName.ROLE_USER)
                .permissions(this.getUserPermissions())
                .build();
    }

    private List<Permission> getUserPermissions() {

        return Collections.singletonList(
                Permission.builder()
                        .name("users:read")
                        .description("Ability to fetch all users")
                        .build()
        );
    }

    public User getUser() {

        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe@library.com")
                .phoneNumber("+2348012345678")
                .role(this.getUserRole())
                .build();
    }

    public Book getBook() {

        return Book.builder()
                .id(2L)
                .createdAt(LocalDateTime.of(2023, 10, 21, 11, 15))
                .title("Things Fall Apart")
                .author("Chinua Achebe")
                .publicationYear(1991)
                .isbn("9123981785")
                .status(BookStatus.AVAILABLE)
                .build();
    }

    public BookResponse getBookResponse() {

        return BookResponse.builder()
                .id(2L)
                .createdAt(LocalDateTime.of(2023, 10, 21, 11, 15))
                .title("Things Fall Apart")
                .author("Chinua Achebe")
                .publicationYear(1991)
                .isbn("9123981785")
                .status(BookStatus.AVAILABLE)
                .build();
    }

    public Patron getPatron() {

        return Patron.builder()
                .id(2L)
                .createdAt(LocalDateTime.of(2023, 5, 18, 15, 22))
                .firstName("Peter")
                .lastName("Obi")
                .contact(ContactDetails.builder()
                        .phoneNumber("+2347088889999")
                        .email("peter.obi@library.com")
                        .build())
                .address(Address.builder()
                        .streetAddress("23 Johnson Street")
                        .city("Ikeja")
                        .state("Lagos")
                        .country("Nigeria")
                        .build())
                .build();
    }

    public PatronResponse getPatronResponse() {

        return PatronResponse.builder()
                .id(2L)
                .createdAt(LocalDateTime.of(2023, 5, 18, 15, 22))
                .firstName("Peter")
                .lastName("Obi")
                .contact(ContactDetails.builder()
                        .phoneNumber("+2347088889999")
                        .email("peter.obi@library.com")
                        .build())
                .address(Address.builder()
                        .streetAddress("23 Johnson Street")
                        .city("Ikeja")
                        .state("Lagos")
                        .country("Nigeria")
                        .build())
                .build();
    }

    public BorrowingRecord getBorrowingRecord() {

        return BorrowingRecord.builder()
                .id(5L)
                .createdAt(LocalDateTime.of(2022, 7, 12, 9, 32))
                .borrowingDate(LocalDate.of(2022, 7, 12))
                .dueDate(LocalDate.of(2022, 7, 19))
                .status(BorrowingRecordStatus.OPEN)
                .book(this.getBook())
                .patron(this.getPatron())
                .build();
    }

    public BorrowingRecordResponse getBorrowingRecordResponse() {

        return BorrowingRecordResponse.builder()
                .id(5L)
                .createdAt(LocalDateTime.of(2022, 7, 12, 9, 32))
                .borrowingDate(LocalDate.of(2022, 7, 12))
                .dueDate(LocalDate.of(2022, 7, 19))
                .status(BorrowingRecordStatus.OPEN)
                .book(this.getBookResponse())
                .patron(this.getPatronResponse())
                .build();
    }

    public Jwt getJwt() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("algo", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("issuedBy", "self");

        return new Jwt(
                this.getAuthenticationResponse().accessToken(),
                Instant.now(),
                Instant.now().plusSeconds(1800L),
                headers,
                claims
        );
    }
}
