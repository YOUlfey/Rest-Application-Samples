package me.youlfey.rest.example.news.portal.service.proxy;

import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.domain.internal.Delta;
import me.youlfey.rest.example.news.portal.domain.jms.Operation;
import me.youlfey.rest.example.news.portal.rep.TopicRepository;
import me.youlfey.rest.example.news.portal.utils.DeltaUtils;
import me.youlfey.rest.example.news.portal.service.NewsService;
import me.youlfey.rest.example.news.portal.service.TopicService;
import me.youlfey.rest.example.news.portal.service.jms.JmsContextProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
@Primary
@Profile("jms")
public class TopicServiceWithJms extends TopicService {
    private final TopicRepository topicRepository;
    private final JmsContextProducer contextProducer;

    public TopicServiceWithJms(TopicRepository topicRepository, NewsService newsService, JmsContextProducer contextProducer) {
        super(topicRepository, newsService);
        this.topicRepository = topicRepository;
        this.contextProducer = contextProducer;
    }

    @Override
    public Topic update(UUID id, Topic updateRequest) throws HttpClientErrorException {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.UPDATE)) {
            Topic topic = getTopicInternal(id);
            Topic oldTopic = Topic.copyDetached(topic);
            updateRequest.setId(id);
            DeltaUtils.update(topic, updateRequest, DeltaUtils.ignoreFields);
            Topic updatedTopic = topicRepository.save(topic);
            Delta<Topic> delta = Delta.produce(oldTopic, updatedTopic);
            ctx.setBody(delta);
            return updatedTopic;
        }
    }

    @Override
    public Topic save(Topic topic) {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.CREATE)) {
            Topic savedTopic = super.save(topic);
            ctx.setBody(savedTopic);
            return savedTopic;
        }
    }

    @Override
    public void remove(UUID id) throws HttpClientErrorException {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.REMOVE)) {
            Topic topic = getById(id);
            ctx.setBody(topic);
            super.remove(id);
        }
    }
}
