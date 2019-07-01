package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    long count();

    void insert(Author author);

    Optional<Author> findByFullName(String firstName, String secondName);

    Optional<Author> getById(int id);

    Optional<Integer> getId(Author author);

    List<Author> getAll();

    boolean update(Author author);

    boolean delete(Author author);
}
