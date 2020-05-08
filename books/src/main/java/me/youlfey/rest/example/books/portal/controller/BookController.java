package me.youlfey.rest.example.books.portal.controller;

import me.youlfey.rest.example.books.portal.domain.Book;
import me.youlfey.rest.example.books.portal.domain.Writer;
import me.youlfey.rest.example.books.portal.service.BookService;
import me.youlfey.rest.example.books.portal.service.WriterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping(value = "/books")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookController {
    private final BookService bookService;
    private final WriterService writerService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Collection<Book>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping(value = "/{writer_id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Collection<Book>> getAllByWriter(@PathVariable Long writer_id) {
        Writer writer = writerService.getById(writer_id);
        return ResponseEntity.ok(bookService.getAllByWriter(writer));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid Book book) {
        try {
            if (Objects.nonNull(book.getWriter()) && Objects.nonNull(book.getWriter().getId())) {
                Writer writer = writerService.getById(book.getWriter().getId());
                book.setWriter(writer);
            }
            return ResponseEntity.ok(bookService.update(id, book));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity removeBook(@PathVariable Long id) {
        try {
            bookService.remove(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @PostMapping(value = "/{writer_id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Book> addBook(@PathVariable Long writer_id, @Valid @RequestBody Book book) {
        try {
            Book saved = writerService.addBook(writer_id, book);
            return ResponseEntity.ok(saved);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
