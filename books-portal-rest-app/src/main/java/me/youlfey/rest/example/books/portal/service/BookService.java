package me.youlfey.rest.example.books.portal.service;

import me.youlfey.rest.example.books.portal.domain.Book;
import me.youlfey.rest.example.books.portal.domain.Writer;
import me.youlfey.rest.example.books.portal.repository.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookService {
    private @NonNull BookRepository bookRepository;

    @Transactional
    public void removeByWriter(Writer writer) {
        bookRepository.removeAllByWriter(writer);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Set<Book> getAllByWriter(@NotNull Writer writer) throws HttpClientErrorException {
        return bookRepository.findAllByWriter(writer);
    }

    public Book update(Long id, Book newBook) throws HttpClientErrorException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        update(book, newBook);
        return bookRepository.save(book);
    }

    private void update(Book updated, Book update) {
        if (Objects.nonNull(update.getDescription())) {
            updated.setDescription(update.getDescription());
        }
        if (Objects.nonNull(update.getCountPages())) {
            updated.setCountPages(update.getCountPages());
        }
        if (Objects.nonNull(update.getName())) {
            updated.setName(update.getName());
        }
        if (Objects.nonNull(update.getWriter()) && Objects.nonNull(update.getWriter().getId())) {
            updated.setWriter(update.getWriter());
        }
    }

    @Transactional
    public void remove(Long id) {
        bookRepository.deleteById(id);
    }
}
