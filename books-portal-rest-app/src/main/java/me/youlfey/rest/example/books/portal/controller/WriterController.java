package me.youlfey.rest.example.books.portal.controller;

import me.youlfey.rest.example.books.portal.domain.Writer;
import me.youlfey.rest.example.books.portal.service.WriterService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(value = "/writers")
public class WriterController {
    @Setter(onMethod_ = @Autowired)
    private WriterService writerService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Collection<Writer>> getAll(@RequestParam(required = false) String firstName,
                                                     @RequestParam(required = false) String lastName) {
        return ResponseEntity.ok(writerService.getAll(firstName, lastName));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Writer> getById(@PathVariable Long id) {
        try {
            Writer writer = writerService.getById(id);
            return ResponseEntity.ok(writer);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Writer> createWriter(@Valid @RequestBody Writer writer) {
        return ResponseEntity.ok(writerService.save(writer));
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Writer> updateWriter(@PathVariable Long id, @RequestBody Writer updWriter) {
        try {
            return ResponseEntity.ok(writerService.update(id, updWriter));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeWriter(@PathVariable Long id) {
        try {
            writerService.remove(id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
