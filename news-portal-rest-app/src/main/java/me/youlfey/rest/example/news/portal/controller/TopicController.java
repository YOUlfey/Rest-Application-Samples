package me.youlfey.rest.example.news.portal.controller;

import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Topic>> getAll(@RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(required = false) Sort.Direction sort) {
        if (Objects.isNull(sort)) {
            return ResponseEntity.ok(topicService.getAll(size, page));
        } else {
            return ResponseEntity.ok(topicService.getAll(size, page, sort));
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> getById(@PathVariable UUID id) {
        Topic topic = topicService.getById(id);
        return ResponseEntity.ok(topic);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> createTopic(@Valid @RequestBody Topic topic) {
        return ResponseEntity.ok(topicService.save(topic));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> updateTopic(@PathVariable UUID id, @Valid @RequestBody Topic updateTopic) {
        Topic topic = topicService.update(id, updateTopic);
        return ResponseEntity.ok(topic);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeTopic(@PathVariable UUID id) {
        topicService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
