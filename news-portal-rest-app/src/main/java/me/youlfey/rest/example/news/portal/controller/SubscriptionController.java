package me.youlfey.rest.example.news.portal.controller;

import me.youlfey.rest.example.news.portal.domain.Subscription;
import me.youlfey.rest.example.news.portal.domain.Topic;
import me.youlfey.rest.example.news.portal.rep.jms.SubscriptionRepository;
import me.youlfey.rest.example.news.portal.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/subscription")
@RequiredArgsConstructor
@Profile("jms")
public class SubscriptionController {
    private final SubscriptionRepository repository;
    private final TopicService topicService;

    @PostMapping(value = "/{id}/subscribe")
    public ResponseEntity subscribe(@PathVariable(value = "id") UUID topicId, @RequestBody Subscription subscription) {
        Topic topic = topicService.getById(topicId);
        subscription.setTopicId(topic.getId());
        return ResponseEntity.ok(repository.save(subscription));
    }

    @DeleteMapping(value = "/{id}/unsubscribe")
    public void unsubscribe(@PathVariable(value = "id") UUID topicId, @RequestParam String email) {
        repository.deleteAllByEmailAndTopicId(email, topicId);
    }
}
