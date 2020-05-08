package me.youlfey.rest.example.books.portal.repository;

import me.youlfey.rest.example.books.portal.domain.Book;
import me.youlfey.rest.example.books.portal.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    void removeAllByWriter(Writer writer);

    Set<Book> findAllByWriter(Writer writer);
}
