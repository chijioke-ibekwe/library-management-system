package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.annotation.WithMockUser;
import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import dev.chijiokeibekwe.librarymanagementsystem.service.impl.JwtServiceImpl;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtServiceImpl.class})
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @MockBean
    private JwtEncoder encoder;

    @MockBean
    private RsaKeyProperties rsaKeyProperties;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @WithMockUser
    public void generateAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArgumentCaptor<JwtEncoderParameters> parametersCapture = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        when(encoder.encode(parametersCapture.capture())).thenReturn(testUtil.getJwt());
        when(rsaKeyProperties.expirationInSeconds()).thenReturn(1800);

        jwtService.generateAccessToken(authentication);

        assertThat(parametersCapture.getValue().getClaims().getClaim("id").toString()).isEqualTo("1");
        assertThat(parametersCapture.getValue().getClaims().getSubject()).isEqualTo("john.doe@library.com");
    }
}