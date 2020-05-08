package me.youlfey.rest.example.artists.portal.service;

import me.youlfey.rest.example.artists.portal.base.BaseService;
import me.youlfey.rest.example.artists.portal.domain.Artist;
import me.youlfey.rest.example.artists.portal.domain.Style;
import me.youlfey.rest.example.artists.portal.exception.HttpNotFoundException;
import me.youlfey.rest.example.artists.portal.repository.ArtistRepository;
import me.youlfey.rest.example.artists.portal.repository.StyleRepository;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;

@Service
public class ArtistService extends BaseService<Artist, UUID> {

    private final StyleRepository styleRepository;

    public ArtistService(StyleRepository styleRepository, ArtistRepository artistRepository) {
        super(artistRepository);
        this.styleRepository = styleRepository;
    }

    @Override
    public Artist insert(@Valid Artist obj) {
        Style style = styleRepository.findById(obj.getStyle().getId()).orElseThrow(HttpNotFoundException::new);
        obj.setStyle(style);
        return super.insert(obj);
    }
}
