package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAll();
}
