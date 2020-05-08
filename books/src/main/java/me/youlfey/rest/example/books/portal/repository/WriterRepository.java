package me.youlfey.rest.example.books.portal.repository;

import me.youlfey.rest.example.books.portal.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {
    Set<Writer> findAllByFirstName(String firstName);
    Set<Writer> findAllByLastName(String lastName);
    Set<Writer> findAllByFirstNameAndLastName(String firstName, String lastName);
}
