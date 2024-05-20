package dev.chijiokeibekwe.librarymanagementsystem.config;

import dev.chijiokeibekwe.librarymanagementsystem.annotation.WithMockUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockUser user)
    {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        List<GrantedAuthority> grantedAuthorities = Stream.of(
                 "users:read",
                        "books:read",
                        "books:write",
                        "books:delete",
                        "patrons:read",
                        "patrons:write",
                        "patrons:delete",
                        "borrowing_records:write"
                )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.email(),
                null,
                grantedAuthorities
        );

        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
