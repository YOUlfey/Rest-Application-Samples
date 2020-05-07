package me.youlfey.rest.example.artists.portal.controller;

import me.youlfey.rest.example.artists.portal.base.BaseController;
import me.youlfey.rest.example.artists.portal.domain.Artist;
import me.youlfey.rest.example.artists.portal.service.ArtistService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/artists")
@Api(value = "Artist api", description = "Rest methods allow to interact with artists")
public class ArtistController extends BaseController<Artist> {

    public ArtistController(ArtistService service) {
        super(service);
    }

    @ApiOperation(value = "Get all artists")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully",
                    response = Artist.class,
                    responseContainer = "List"),
            @ApiResponse(code = 204, message = "Nothing", response = void.class)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return super.getAll();
    }

    @ApiOperation(value = "Get all artists by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = Artist.class,
                    responseContainer = "List"),
            @ApiResponse(code = 204, message = "Nothing", response = void.class)
    })
    @GetMapping(value = "/get-all-by-name", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAllByName(@RequestParam @ApiParam(name = "Artist name") String name) {
        return super.getAllByName(name);
    }

    @ApiOperation(value = "Create artist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = Artist.class)
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody @ApiParam(name = "Request body for create") Artist artist) {
        return super.add(artist);
    }

    @ApiOperation(value = "Update artist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = Artist.class),
            @ApiResponse(code = 204, message = "Artist not found", response = void.class)
    })
    @PatchMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(@PathVariable @ApiParam(name = "Artist identifier") UUID id,
                                 @RequestBody @ApiParam(name = "Request body for update") Artist artist) {
        return super.update(id, artist);
    }

    @ApiOperation(value = "Remove artist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = void.class),
            @ApiResponse(code = 204, message = "Artist not found", response = void.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable @ApiParam(name = "Artist identifier") UUID id) {
        return super.delete(id);
    }

    @ApiOperation(value = "Get artist by identifier")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = Artist.class),
            @ApiResponse(code = 204, message = "Artist not found", response = void.class)
    })
    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable @ApiParam(name = "Artist identifier") UUID id) {
        return super.get(id);
    }
}
