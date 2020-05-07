package me.youlfey.rest.example.news.portal.service.proxy;

import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.domain.jms.Operation;
import me.youlfey.rest.example.news.portal.domain.response.UpdateResponse;
import me.youlfey.rest.example.news.portal.rep.NewsRepository;
import me.youlfey.rest.example.news.portal.service.NewsService;
import me.youlfey.rest.example.news.portal.service.jms.JmsContextProducer;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@Profile("jms")
public class NewsServiceWithJms extends NewsService {
    private final NewsRepository newsRepository;
    private final JmsContextProducer contextProducer;

    public NewsServiceWithJms(NewsRepository newsRepository, JmsContextProducer contextProducer) {
        super(newsRepository);
        this.newsRepository = newsRepository;
        this.contextProducer = contextProducer;
    }

    @Override
    public News save(News news) {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.CREATE)) {
            news = super.save(news);
            ctx.setBody(news);
            return news;
        }
    }

    @Override
    public List<News> save(List<News> news) {
        news = super.save(news);
        news.forEach(singleNews -> {
            try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.CREATE)) {
                ctx.setBody(singleNews);
            }
        });
        return news;
    }

    @Override
    public UpdateResponse<News> update(UUID id, News newNews) throws HttpClientErrorException {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.UPDATE)) {
            UpdateResponse<News> response = super.update(id, newNews);
            ctx.setBody(response.getDelta());
            return response;
        }
    }

    @Override
    public void remove(UUID id) {
        try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.REMOVE)) {
            News news = newsRepository.mustFindById(id);
            ctx.setBody(news);
            super.remove(id);
        }
    }

    @Override
    public void removeByTopic(Topic topic) {
        int index = 0;
        int size = 10;
        Page<News> page = getAllByTopic(topic, size, index);
        Iterator<News> currentPageIterator = page.iterator();
        while (currentPageIterator.hasNext() || page.hasNext()) {
            try (JmsContextProducer.JmsContext ctx = contextProducer.buildBaseContext(Operation.REMOVE)) {
                if (!currentPageIterator.hasNext()) {
                    index++;
                    page = getAllByTopic(topic, size, index);
                    currentPageIterator = page.iterator();
                }
                News news = currentPageIterator.next();
                ctx.setBody(news);
            }
        }
        super.removeByTopic(topic);
    }
}
