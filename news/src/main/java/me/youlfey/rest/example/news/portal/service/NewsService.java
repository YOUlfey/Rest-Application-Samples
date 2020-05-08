package me.youlfey.rest.example.news.portal.service;

import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.domain.internal.Delta;
import me.youlfey.rest.example.news.portal.domain.response.UpdateResponse;
import me.youlfey.rest.example.news.portal.rep.NewsRepository;
import me.youlfey.rest.example.news.portal.utils.DeltaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {
    private static final String MODIFIED_DATE = "modifiedDate";
    private final NewsRepository newsRepository;

    public void removeByTopic(Topic topic) {
        newsRepository.removeAllByTopic(topic);
    }

    public News save(News news) {
        return newsRepository.save(news);
    }

    public List<News> save(List<News> news) {
        return newsRepository.saveAll(news);
    }

    public Page<News> getAll(Integer size, Integer page) {
        PageRequest pageable = PageRequest.of(page, size);
        return newsRepository.findAll(pageable);
    }

    public Page<News> getAll(Integer size, Integer page, Sort.Direction sort) {
        PageRequest pageable = PageRequest.of(page, size, sort, MODIFIED_DATE);
        return newsRepository.findAll(pageable);
    }

    public Page<News> getAllByTopic(@NotNull Topic topic, Integer size, Integer page) throws HttpClientErrorException {
        PageRequest pageable = PageRequest.of(page, size);
        return newsRepository.findAllByTopic(pageable, topic);
    }

    public Page<News> getAllByTopic(Topic topic, Integer size, Integer page, Sort.Direction sort) throws HttpClientErrorException {
        PageRequest pageable = PageRequest.of(page, size, sort, MODIFIED_DATE);
        return newsRepository.findAllByTopic(pageable, topic);
    }

    public UpdateResponse<News> update(UUID id, News newNews) throws HttpClientErrorException {
        News news = newsRepository.mustFindById(id);
        newNews.setId(id);
        News oldNews = News.copyDetached(news);
        DeltaUtils.update(news, newNews, DeltaUtils.ignoreFields);
        if (!oldNews.equals(news)) {
            News savedNews = newsRepository.save(news);
            Delta<News> delta = Delta.produce(oldNews, savedNews);
            return new UpdateResponse<>(true, delta);
        } else {
            return new UpdateResponse<>();
        }
    }

    public void remove(UUID id) {
        newsRepository.deleteById(id);
    }
}
