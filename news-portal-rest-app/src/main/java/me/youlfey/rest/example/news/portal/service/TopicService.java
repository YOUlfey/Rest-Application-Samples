package me.youlfey.rest.example.news.portal.service;

import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.rep.TopicRepository;
import me.youlfey.rest.example.news.portal.utils.DeltaUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {
    private static final String MODIFIED_DATE = "modifiedDate";
    private final TopicRepository topicRepository;
    private final NewsService newsService;

    public Page<Topic> getAll(Integer size, Integer page) {
        PageRequest pageable = PageRequest.of(page, size);
        return topicRepository.findAll(pageable);
    }

    public Topic getById(UUID id) {
        return getTopicInternal(id);
    }

    public Topic save(Topic topic) {
        topic.setId(null);
        return topicRepository.save(topic);
    }

    public Topic update(UUID id, Topic updateRequest) {
        Topic topic = getTopicInternal(id);
        updateRequest.setId(id);
        DeltaUtils.update(topic, updateRequest, DeltaUtils.ignoreFields);
        return topicRepository.save(topic);
    }

    @NotNull
    protected Topic getTopicInternal(UUID id) {
        return topicRepository.mustFindById(id);
    }

    public void remove(UUID id) {
        Topic topic = getTopicInternal(id);
        newsService.removeByTopic(topic);
        topicRepository.deleteById(id);
    }

    public News addNews(UUID id, News news) {
        Topic topic = getTopicInternal(id);
        news.setTopic(topic);
        return newsService.save(news);
    }

    public List<News> addNews(UUID id, List<News> news) {
        Topic topic = getTopicInternal(id);
        news.forEach(item -> item.setTopic(topic));
        return newsService.save(news);
    }

    public Page<Topic> getAll(Integer size, Integer page, Sort.Direction sort) {
        PageRequest pageable = PageRequest.of(page, size, sort, MODIFIED_DATE);
        return topicRepository.findAll(pageable);
    }
}
