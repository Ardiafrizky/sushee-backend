package com.future.sushee.service;

import com.future.sushee.model.Menu;
import com.future.sushee.service.implementations.EmailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@Tag("service")
public class EmailServiceTest {

    @InjectMocks
    EmailServiceImpl emailService;

    @Mock
    JavaMailSender javaMailSender;

    @Captor
    ArgumentCaptor<SimpleMailMessage> mailCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendEmailTest() {
        emailService.sendEmail("to", 1L, "username");

        verify(javaMailSender).send(mailCaptor.capture());
        SimpleMailMessage mail = mailCaptor.getValue();
        assertTrue(Arrays.asList(Objects.requireNonNull(mail.getTo())).contains("to"));
    }
}
