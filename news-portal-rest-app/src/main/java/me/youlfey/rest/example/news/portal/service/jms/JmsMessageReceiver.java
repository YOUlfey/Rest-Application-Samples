package me.youlfey.rest.example.news.portal.service.jms;

import me.youlfey.rest.example.news.portal.config.jms.Destinations;
import me.youlfey.rest.example.news.portal.domain.jms.JmsMessage;
import me.youlfey.rest.example.news.portal.domain.jms.LetterInstance;
import me.youlfey.rest.example.news.portal.domain.jms.LogOperation;
import me.youlfey.rest.example.news.portal.rep.jms.LoggingRepository;
import me.youlfey.rest.example.news.portal.service.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("jms")
public class JmsMessageReceiver {
    private final LoggingRepository loggingRepository;
    private final EmailSender mailSender;
    private final Map<Class, Consumer> sendConsumers = new HashMap<Class, Consumer>() {{
        put(LetterInstance.class, (Consumer<LetterInstance>) (letterInstance) -> receiveMessageEmail(letterInstance));
        put(LogOperation.class, (Consumer<LogOperation>) messageLog -> receiveMessageLog(messageLog));
    }};

    @JmsListener(destination = Destinations.DEFAULT_DESTINATION)
    private void receive(JmsMessage message) {
        sendConsumers.getOrDefault(message.getType(),
                object -> log.warn("The message can't be handled"))
                .accept(message);
    }

    private void receiveMessageLog(LogOperation log) {
        loggingRepository.save(log);
    }

    private void receiveMessageEmail(LetterInstance letterInstance) {
        if (Objects.nonNull(letterInstance.getDestinations()) && letterInstance.getDestinations().length > 0) {
            mailSender.send(letterInstance);
        } else {
            log.info("letter can't be send, by reason empty destination");
        }
    }
}
