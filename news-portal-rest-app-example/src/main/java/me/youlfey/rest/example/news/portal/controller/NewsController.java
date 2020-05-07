package me.youlfey.rest.example.news.portal.controller;

import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.domain.response.UpdateResponse;
import me.youlfey.rest.example.news.portal.service.NewsService;
import me.youlfey.rest.example.news.portal.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<Page<News>> getAll(@RequestParam(defaultValue = "10") Integer size,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(required = false) Sort.Direction sort) {
        if (Objects.isNull(sort)) {
            return ResponseEntity.ok(newsService.getAll(size, page));
        } else {
            return ResponseEntity.ok(newsService.getAll(size, page, sort));
        }
    }

    @GetMapping(value = "/topics/{topicId}")
    public ResponseEntity<Page<News>> geAllByTopic(@RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(required = false) Sort.Direction sort,
                                                   @PathVariable UUID topicId) {
        Topic topic = topicService.getById(topicId);
        if (Objects.isNull(sort)) {
            return ResponseEntity.ok(newsService.getAllByTopic(topic, size, page));
        } else {
            return ResponseEntity.ok(newsService.getAllByTopic(topic, size, page, sort));
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UpdateResponse<News>> updateNews(@PathVariable UUID id, @RequestBody @Valid News news) {
        if (Objects.nonNull(news.getTopic()) && Objects.nonNull(news.getTopic().getId())) {
            Topic topic = topicService.getById(news.getTopic().getId());
            news.setTopic(topic);
        }
        return ResponseEntity.ok(newsService.update(id, news));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeNews(@PathVariable UUID id) {
        newsService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/topics/{topicId}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<News> addNews(@PathVariable UUID topicId, @Valid @RequestBody News news) {
        News savedNews = topicService.addNews(topicId, news);
        return ResponseEntity.ok(savedNews);
    }

    @PostMapping(value = "/topics/bulk/{topicId}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<News>> addNews(@PathVariable UUID topicId, @Valid @RequestBody List<News> newsList) {
        List<News> savedNews = topicService.addNews(topicId, newsList);
        return ResponseEntity.ok(savedNews);
    }
}
