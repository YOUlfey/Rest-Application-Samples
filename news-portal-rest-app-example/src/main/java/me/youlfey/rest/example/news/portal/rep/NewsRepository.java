package me.youlfey.rest.example.news.portal.rep;

import me.youlfey.rest.example.news.portal.domain.News;
import me.youlfey.rest.example.news.portal.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {
    Page<News> findAllByTopic(Pageable page, Topic topic);

    @Transactional
    void removeAllByTopic(Topic topic);

    default News mustFindById(UUID id) {
        return findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}
