package me.youlfey.rest.example.news.portal.service.jms;

import me.youlfey.rest.example.news.portal.domain.jms.Operation;
import lombok.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Profile("jms")
public class JmsContextProducer {

    private final JmsSenderService senderService;

    public JmsContext buildBaseContext(Operation operation) {
        return new JmsContext(operation, senderService);
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @ToString
    public static class JmsContext implements AutoCloseable {
        @Setter
        private Object body;
        private final Operation operation;
        private final JmsSenderService sender;

        @Override
        public void close() {
            if (Objects.nonNull(body)) {
                sender.send(JmsSenderService.OperationMessage.of(operation, body));
            }
        }
    }
}
