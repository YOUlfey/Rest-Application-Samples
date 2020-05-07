package me.youlfey.rest.example.artists.portal.repository;

import me.youlfey.rest.example.artists.portal.base.BaseRepository;
import me.youlfey.rest.example.artists.portal.domain.Style;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StyleRepository extends BaseRepository<Style, UUID> {
    List<Style> findAllByName(String name);
}
