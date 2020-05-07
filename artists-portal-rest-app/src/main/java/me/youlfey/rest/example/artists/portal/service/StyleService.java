package me.youlfey.rest.example.artists.portal.service;

import me.youlfey.rest.example.artists.portal.base.BaseService;
import me.youlfey.rest.example.artists.portal.domain.Style;
import me.youlfey.rest.example.artists.portal.repository.ArtistRepository;
import me.youlfey.rest.example.artists.portal.repository.StyleRepository;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
public class StyleService extends BaseService<Style, UUID> {
    private final ArtistRepository artistRepository;

    public StyleService(ArtistRepository artistRepository, StyleRepository styleRepository) {
        super(styleRepository);
        this.artistRepository = artistRepository;
    }

    @Override
    public void remove(@NotNull UUID id) {
        Style style = getById(id);
        artistRepository.deleteAllByStyle(style);
        super.remove(id);
    }

    @Override
    public Style insert(@Valid Style style) {
        return super.insert(style);
    }
}
