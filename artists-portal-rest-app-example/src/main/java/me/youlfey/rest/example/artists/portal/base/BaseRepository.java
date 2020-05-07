package me.youlfey.rest.example.artists.portal.base;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseRepository<T extends Base, S> extends JpaRepository<T, S> {
    List<T> findAllByName(String name);
}
