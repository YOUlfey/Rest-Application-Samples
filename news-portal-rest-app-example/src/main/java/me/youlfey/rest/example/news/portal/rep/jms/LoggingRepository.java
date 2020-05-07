package me.youlfey.rest.example.news.portal.rep.jms;

import me.youlfey.rest.example.news.portal.domain.jms.LogOperation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("jms")
public interface LoggingRepository extends JpaRepository<LogOperation, UUID> {
}
