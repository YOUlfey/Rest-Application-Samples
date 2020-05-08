package me.youlfey.rest.example.artists.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.youlfey.rest.example.artists.portal.base.BaseController;
import me.youlfey.rest.example.artists.portal.domain.Style;
import me.youlfey.rest.example.artists.portal.service.StyleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/styles")
@Api(value = "styles api", description = "Rest methods allow to interact with styles")
public class StyleController extends BaseController<Style> {

    public StyleController(StyleService service) {
        super(service);
    }

    @ApiOperation(value = "Get all styles",
            responseContainer = "List",
            response = Style.class,
            produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 204, message = "Nothing")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return super.getAll();
    }

    @ApiOperation(value = "Get all styles by name",
            response = Style.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 204, message = "Nothing")
    })
    @GetMapping(value = "/get-all-by-name", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAllByName(@RequestParam String name) {
        return super.getAllByName(name);
    }

    @ApiOperation(value = "Create style",
            response = Style.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully")
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody Style style) {
        return super.add(style);
    }


    @ApiOperation(value = "Update style",
            response = Style.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 204, message = "Style not found")
    })
    @PatchMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(@PathVariable UUID id,
                                 @RequestBody Style style) {
        return super.update(id, style);
    }

    @ApiOperation(value = "Remove style")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = void.class),
            @ApiResponse(code = 204, message = "Style not found")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        return super.delete(id);
    }

    @ApiOperation(value = "Get style by identifier",
            response = Style.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = Style.class),
            @ApiResponse(code = 204, message = "Style not found")
    })
    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable UUID id) {
        return super.get(id);
    }
}
