package me.youlfey.rest.example.news.portal.service.jms;

import me.youlfey.rest.example.news.portal.config.jms.Destinations;
import me.youlfey.rest.example.news.portal.domain.jms.LogOperation;
import me.youlfey.rest.example.news.portal.domain.jms.Operation;
import me.youlfey.rest.example.news.portal.utils.LetterTransformer;
import me.youlfey.rest.example.news.portal.utils.LogTransformer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("jms")
public class JmsSenderService {
    private final JmsTemplate jmsTemplate;
    private final LogTransformer logTransformer;
    private final LetterTransformer letterTransformer;

    <T> void send(OperationMessage<T> message) {
        sendLetterInstance(message);
        sendLogOperation(message);
    }

    private <T> void sendLetterInstance(OperationMessage<T> message) {
        letterTransformer.transform(message)
                .ifPresent(letterInstance ->
                        jmsTemplate.convertAndSend(Destinations.DEFAULT_DESTINATION, letterInstance));
    }

    private <T> void sendLogOperation(OperationMessage<T> message) {
        LogOperation logOperation = logTransformer.transform(message);
        jmsTemplate.convertAndSend(Destinations.DEFAULT_DESTINATION, logOperation);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationMessage<T> {
        private Operation operation;
        private T body;

        static <T> OperationMessage<T> of(Operation operation, T body) {
            return new OperationMessage<>(operation, body);
        }
    }
}
