package me.youlfey.rest.example.books.portal.service;

import me.youlfey.rest.example.books.portal.domain.Book;
import me.youlfey.rest.example.books.portal.domain.Writer;
import me.youlfey.rest.example.books.portal.repository.WriterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WriterService {
    private @NonNull WriterRepository writerRepository;
    private @NonNull BookService bookService;

    public Collection<Writer> getAll(String firstName, String lastName) {
        if (Objects.nonNull(firstName) && Objects.nonNull(lastName)) {
            return getAllByFirstAndLastNames(firstName, lastName);
        } else {
            if (Objects.nonNull(firstName)) {
                return getAllByFirstName(firstName);
            } else if (Objects.nonNull(lastName)) {
                return getAllByLastName(lastName);
            } else {
                return getAll();
            }
        }
    }

    private List<Writer> getAll() {
        return writerRepository.findAll();
    }

    private Set<Writer> getAllByFirstName(String firstName) {
        return writerRepository.findAllByFirstName(firstName);
    }

    private Set<Writer> getAllByLastName(String lastName) {
        return writerRepository.findAllByLastName(lastName);
    }

    private Set<Writer> getAllByFirstAndLastNames(String firstName, String lastName) {
        return writerRepository.findAllByFirstNameAndLastName(firstName, lastName);
    }

    public Writer getById(Long id) throws HttpClientErrorException {
        return writerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public Writer save(Writer writer) {
        writer.setId(null);
        return writerRepository.save(writer);
    }

    public Writer update(Long id, Writer updatedWriter) throws HttpClientErrorException {
        Writer writer = writerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Writer not found by id"));
        update(writer, updatedWriter);
        return writerRepository.save(writer);
    }

    private void update(Writer updated, Writer update) {
        if (Objects.nonNull(update.getFirstName())) {
            updated.setFirstName(update.getFirstName());
        }
        if (Objects.nonNull(update.getLastName())) {
            updated.setLastName(update.getLastName());
        }
    }

    @Transactional
    public void remove(Long id) throws HttpClientErrorException {
        Writer writer = writerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Writer not found"));
        bookService.removeByWriter(writer);
        writerRepository.deleteById(id);
    }

    public Book addBook(Long id, Book book) throws HttpClientErrorException {
        Writer writer = writerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Writer not found"));
        book.setWriter(writer);
        return bookService.save(book);
    }
}
