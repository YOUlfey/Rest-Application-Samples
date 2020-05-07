package me.youlfey.rest.example.news.portal.utils;

import me.youlfey.rest.example.news.portal.config.email.EmailTemplates;
import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.domain.internal.Delta;
import me.youlfey.rest.example.news.portal.domain.jms.LetterInstance;
import me.youlfey.rest.example.news.portal.domain.jms.Operation;
import me.youlfey.rest.example.news.portal.domain.projection.EmailProjection;
import me.youlfey.rest.example.news.portal.rep.jms.SubscriptionRepository;
import me.youlfey.rest.example.news.portal.service.jms.JmsSenderService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;

import java.util.*;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
@Profile("jms")
public class LetterTransformer {

    private final SubscriptionRepository subscriptionRepository;

    public Optional<LetterInstance> transform(JmsSenderService.OperationMessage message) {
        if (Objects.isNull(message) || Objects.isNull(message.getBody()) || Objects.isNull(message.getOperation())) {
            return Optional.empty();
        }
        Optional<LetterKey> letterKey = LetterKey.valueOf(message.getOperation(), message.getBody());
        if (letterKey.isPresent() && letterProducers.containsKey(letterKey.get())) {
            return letterProducers.get(letterKey.get()).apply(message);
        }
        return Optional.empty();
    }


    private Optional<LetterInstance> getLetterInstanceForNewsDuringUpdate(JmsSenderService.OperationMessage message) {
        Delta delta = (Delta) message.getBody();
        if (!delta.isChanged()) {
            return Optional.empty();
        }
        if (delta.getNewValue() instanceof News) {
            News newValue = (News) delta.getNewValue();
            ST template = EmailTemplates.templates.get(EmailTemplates.UPDATE_NEWS);
            template.add(EmailTemplates.TemplateParameters.ID, newValue.getId());
            if (delta instanceof Delta.ComplexDelta) {
                Map<String, Delta> deltaMap = ((Delta.ComplexDelta<News>) delta).getDeltaMap();
                if (Objects.nonNull(deltaMap.get(EmailTemplates.TemplateParameters.MESSAGE))) {
                    if (deltaMap.get(EmailTemplates.TemplateParameters.MESSAGE).isChanged()) {
                        template.add(EmailTemplates.TemplateParameters.MESSAGE_CONDITION, true);
                        template.add(EmailTemplates.TemplateParameters.MESSAGE, newValue.getMessage());
                    }
                } else {
                    template.add(EmailTemplates.TemplateParameters.MESSAGE_CONDITION, false);
                }
                if (Objects.nonNull(deltaMap.get(EmailTemplates.TemplateParameters.TOPIC))) {
                    if (deltaMap.get(EmailTemplates.TemplateParameters.TOPIC).isChanged()) {
                        template.add(EmailTemplates.TemplateParameters.TOPIC_CONDITION, true);
                        template.add(EmailTemplates.TemplateParameters.TOPIC_ID, newValue.getTopic().getId());
                    }
                } else {
                    template.add(EmailTemplates.TemplateParameters.TOPIC_CONDITION, false);
                }
            }
            String content = template.render();
            STUtils.removeSpentAttributes(template,
                    EmailTemplates.TemplateParameters.ID,
                    EmailTemplates.TemplateParameters.MESSAGE,
                    EmailTemplates.TemplateParameters.MESSAGE_CONDITION,
                    EmailTemplates.TemplateParameters.TOPIC_CONDITION,
                    EmailTemplates.TemplateParameters.TOPIC_ID);

            String[] destinations = getDestinationsForTopic(newValue.getTopic());
            return Optional.of(new LetterInstance(EmailTemplates.UPDATE_NEWS, content, destinations));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Optional<LetterInstance> getLetterInstanceForTopicDuringUpdate(JmsSenderService.OperationMessage message) {
        Delta delta = (Delta) message.getBody();
        if (!delta.isChanged()) {
            return Optional.empty();
        }
        if (delta.getNewValue() instanceof Topic) {
            Topic newTopic = (Topic) delta.getNewValue();
            ST template = EmailTemplates.templates.get(EmailTemplates.UPDATE_TOPIC);
            template.add(EmailTemplates.TemplateParameters.ID, newTopic);
            template.add(EmailTemplates.TemplateParameters.NAME, newTopic);
            template.add(EmailTemplates.TemplateParameters.DATE, newTopic);
            String content = template.render();
            STUtils.removeSpentAttributes(template,
                    EmailTemplates.TemplateParameters.ID, EmailTemplates.TemplateParameters.NAME, EmailTemplates.TemplateParameters.DATE);
            String[] destinations = getDestinationsForTopic(newTopic);
            return Optional.of(new LetterInstance(EmailTemplates.UPDATE_TOPIC, content, destinations));
        } else {
            throw new IllegalArgumentException();
        }
    }

    @NotNull
    // Only for remove and create, not use for update
    private Optional<LetterInstance> getLetterInstanceForNews(JmsSenderService.OperationMessage message, String letterSubject) {
        News news = (News) message.getBody();
        Topic topic = news.getTopic();
        String[] destinations = getDestinationsForTopic(topic);
        return Optional.of(createLetterForNews(message, letterSubject, destinations));
    }

    @NotNull
    // Only for remove and create, not use for update
    private Optional<LetterInstance> getLetterInstanceForTopic(JmsSenderService.OperationMessage message, String letterSubject) {
        Topic topic = (Topic) message.getBody();
        String[] destinations = getDestinationsForTopic(topic);
        return Optional.of(createLetterForTopic(message, letterSubject, destinations));
    }

    private Optional<LetterInstance> getLetterInstanceForNewsDuringCreate(JmsSenderService.OperationMessage message) {
        return getLetterInstanceForNews(message, EmailTemplates.CREATE_NEWS);
    }

    private Optional<LetterInstance> getLetterInstanceForNewsDuringRemove(JmsSenderService.OperationMessage message) {
        return getLetterInstanceForNews(message, EmailTemplates.REMOVE_NEWS);
    }

    private Optional<LetterInstance> getLetterInstanceForTopicDuringCreate(JmsSenderService.OperationMessage message) {
        return getLetterInstanceForTopic(message, EmailTemplates.CREATE_TOPIC);
    }

    private Optional<LetterInstance> getLetterInstanceForTopicDuringRemove(JmsSenderService.OperationMessage message) {
        Optional<LetterInstance> letter = getLetterInstanceForTopic(message, EmailTemplates.REMOVE_TOPIC);
        Topic topic = (Topic) message.getBody();
        UUID topicId = topic.getId();
        subscriptionRepository.deleteAllByTopicId(topicId);
        return letter;
    }

    private String[] getDestinationsForTopic(Topic topic) {
        UUID topicId = topic.getId();
        return subscriptionRepository.findAllByTopicId(topicId).stream()
                .map(EmailProjection::getEmail)
                .toArray(String[]::new);
    }

    @NotNull
    private static LetterInstance createLetterForTopic(JmsSenderService.OperationMessage message, String templateName, String[] destinations) {
        ST template = EmailTemplates.templates.get(templateName);
        template.add(EmailTemplates.TemplateParameters.ID, ((Topic) message.getBody()).getId());
        template.add(EmailTemplates.TemplateParameters.NAME, ((Topic) message.getBody()).getName());
        String content = template.render();
        STUtils.removeSpentAttributes(template, EmailTemplates.TemplateParameters.ID, EmailTemplates.TemplateParameters.NAME);
        return new LetterInstance(templateName, content, destinations);
    }

    @NotNull
    private static LetterInstance createLetterForNews(JmsSenderService.OperationMessage message, String templateName, String[] destinations) {
        ST template = EmailTemplates.templates.get(templateName);
        template.add(EmailTemplates.TemplateParameters.ID, ((News) message.getBody()).getId());
        template.add(EmailTemplates.TemplateParameters.MESSAGE, ((News) message.getBody()).getMessage());
        template.add(EmailTemplates.TemplateParameters.TOPIC_ID, ((News) message.getBody()).getTopic().getId());
        template.add(EmailTemplates.TemplateParameters.TOPIC_NAME, ((News) message.getBody()).getTopic().getName());
        String content = template.render();
        STUtils.removeSpentAttributes(template,
                EmailTemplates.TemplateParameters.ID, EmailTemplates.TemplateParameters.NAME,
                EmailTemplates.TemplateParameters.TOPIC_ID, EmailTemplates.TemplateParameters.TOPIC_NAME);
        return new LetterInstance(templateName, content, destinations);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private enum LetterKey {
        CREATE_TOPIC(Operation.CREATE, Topic.class),
        CREATE_NEWS(Operation.CREATE, News.class),
        REMOVE_TOPIC(Operation.REMOVE, Topic.class),
        REMOVE_NEWS(Operation.REMOVE, News.class),
        UPDATE_TOPIC(Operation.UPDATE, Topic.class),
        UPDATE_NEWS(Operation.UPDATE, News.class);

        public static Optional<LetterKey> valueOf(Operation operation, Object domainObj) {
            Objects.requireNonNull(operation);
            Objects.requireNonNull(domainObj);

            final Class domainClass = domainObj instanceof Delta ? ((Delta) domainObj).getType() : domainObj.getClass();

            return getAllLetterKeys().stream()
                    .filter(key -> operation.equals(key.getOperation()) && domainClass.equals(key.getDomainClass()))
                    .findFirst();
        }

        public static Set<LetterKey> getAllLetterKeys() {
            return ImmutableSet.copyOf(values());
        }

        private final Operation operation;
        private final Class domainClass;
    }

    private Map<LetterKey, Function<JmsSenderService.OperationMessage, Optional<LetterInstance>>> letterProducers = ImmutableMap.copyOf(
            new HashMap<LetterKey, Function<JmsSenderService.OperationMessage, Optional<LetterInstance>>>() {{
                put(LetterKey.CREATE_NEWS, jmsMessage -> getLetterInstanceForNewsDuringCreate(jmsMessage));
                put(LetterKey.CREATE_TOPIC, jmsMessage -> getLetterInstanceForTopicDuringCreate(jmsMessage));
                put(LetterKey.REMOVE_NEWS, jmsMessage -> getLetterInstanceForNewsDuringRemove(jmsMessage));
                put(LetterKey.REMOVE_TOPIC, jmsMessage -> getLetterInstanceForTopicDuringRemove(jmsMessage));
                put(LetterKey.UPDATE_TOPIC, jmsMessage -> getLetterInstanceForTopicDuringUpdate(jmsMessage));
                put(LetterKey.UPDATE_NEWS, jmsMessage -> getLetterInstanceForNewsDuringUpdate(jmsMessage));
            }}
    );

    static class STUtils {
        private static void removeAttrIfPresent(ST st, String attr) {
            if (Objects.nonNull(st.getAttribute(attr))) {
                st.remove(attr);
            }
        }

        static void removeSpentAttributes(ST st, String... attrs) {
            for (String attr : attrs) {
                removeAttrIfPresent(st, attr);
            }
        }
    }
}
