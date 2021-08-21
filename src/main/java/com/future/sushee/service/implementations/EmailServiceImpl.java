package com.future.sushee.service.implementations;

import com.future.sushee.service.interfaces.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
@Transactional
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Async
    @Override
    public void sendEmail(String to, Long id, String username) {
        SimpleMailMessage mail = new SimpleMailMessage();
        String url = "http://localhost:8000/api/reservation/" + id + "/update-status";

        String message = "Dear, " + username + "\n\n";
        message = message + "Your reservation has been successfully generated, with ID: " + id + ".\n";
        message = message + "On appointed time, please scan the QRCode below to start the reservation session.\n";
        message = message + "QRCode: " + "https://api.qrserver.com/v1/create-qr-code/?data=" + url + "&size=100x100";
        message = message + "\n\nThanks for your trust, enjoy.\nBest regards,\nSushee Restaurant";

        mail.setFrom("sushee.restaurant@gmail.com");
        mail.setTo(to);
        mail.setSubject("Reservation Message, ID: "+id);
        mail.setText(message);
        emailSender.send(mail);
    }
}
