package me.youlfey.rest.example.news.portal.service.email;

import me.youlfey.rest.example.news.portal.domain.jms.LetterInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component(value = "jmsMailSender")
@RequiredArgsConstructor
@Slf4j
@Profile("jms")
public class EmailSender {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void send(LetterInstance letterInstance) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(letterInstance.getDestinations());
        message.setFrom(username);
        message.setSubject(letterInstance.getSubject());
        message.setText(letterInstance.getContent());
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Occurred error during send message", e);
        }
    }
}
