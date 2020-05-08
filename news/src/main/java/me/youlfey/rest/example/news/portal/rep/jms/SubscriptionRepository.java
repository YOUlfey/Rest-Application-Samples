package me.youlfey.rest.example.news.portal.rep.jms;

import me.youlfey.rest.example.news.portal.domain.Subscription;
import me.youlfey.rest.example.news.portal.domain.projection.EmailProjection;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Profile("jms")
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<EmailProjection> findAllByTopicId(UUID topicId);

    void deleteAllByEmailAndTopicId(String email, UUID topicId);

    @Transactional
    void deleteAllByTopicId(UUID topicId);
}
