package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    List<Author> findAll();

    Author save(Author author);
}
